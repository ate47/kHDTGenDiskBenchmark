#!/usr/bin/env bash

OUTPUT=/data2/gentest/

mkdir $OUTPUT

for iter in $(seq 10); do
    echo "[$iter] start iteration"

    rm -rf datasetgen

    for file in *.nt; do
        echo "[$iter] indexing $file, cloning dataset"
	mkdir -p datasetgen
	ln $file datasetgen/$file

        ls -lh datasetgen

        java -Xmx10G -cp bench.jar com.the_qa_company.benchmark.CliMain lubm datasetgen "$OUTPUT$file.hdt"
        mkdir -p "profiling-$iter"
        mv "$OUTPUT$file.hdt.prof" "profiling-$iter"
    done

done

echo "end iterations"
