# kHDTGenDiskBenchmark
Experiments scripts for the benchmark used in k-HDTCat

## Lubm generation

[In the lubmgen directory](lubmgen/README.md)

## Wikidata generation

[Done using the CLI](https://github.com/the-qa-company/qEndpoint/wiki/qEndpoint-CLI-Indexing-datasets).

## Wiki changes

In [wiki-changes](https://github.com/the-qa-company/wiki-changes). The user should have a precomputed Wikidata truthy HDT. See [Wikidata generation](#wikidata-generation).

Compile it with `./gradlew shadowJar`.

Create delta file using

```pwsh
java -cp wiki-changes.jar com.the_qa_company.wikidatachanges.WikidataChangesDelta --date 2023-10-31T00:40:00Z -f simple -m 0 -S 10000
```

`2023-10-31T00:40:00Z` is the date to rollback

I'll create a delta.df file

```pwsh
## PREPARE DATA

# generate an hdt from the delta file
rdf2hdt -config delta.df delta.hdt -multithread -color
# Split 
java -cp wiki-changes.jar com.the_qa_company.wikidatachanges.WikidataChangesCompute div delta.hdt 7
# Compute delete bitmap
java -cp wiki-changes.jar com.the_qa_company.wikidatachanges.WikidataChangesCompute bitmap delta.hdt wikidata-truthy.hdt outbitmap.bin

## BENCHMARK

# Cat only test
java -cp wiki-changes.jar com.the_qa_company.wikidatachanges.WikidataChangesCompute catonly diff.hdt delta.hdt

# Merge diff test
java -cp wiki-changes.jar com.the_qa_company.wikidatachanges.WikidataChangesCompute mergediff wikidata-truthy.hdt outbitmap.bin delta.hdt 7

# Diff only test
java -cp wiki-changes.jar com.the_qa_company.wikidatachanges.WikidataChangesCompute diffonly wikidata-truthy.hdt outbitmap.bin

# CatDiff test
java -cp wiki-changes.jar com.the_qa_company.wikidatachanges.WikidataChangesCompute catdiffonly wikidata-truthy.hdt outbitmap.bin delta.hdt
```