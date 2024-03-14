## LUBM generation

First generate the LUBM files you want to test using the script [jm-gimenez-garcia/lubm-batch](https://github.com/jm-gimenez-garcia/lubm-batch) into .nt form.

The experiments were ran using this dataset

```
19G lubm01000.nt
19G lubm02000.nt
19G lubm03000.nt
19G lubm04000.nt
19G lubm05000.nt
19G lubm06000.nt
19G lubm07000.nt
19G lubm08000.nt
76G lubm12000.nt
77G lubm16000.nt
77G lubm20000.nt
77G lubm24000.nt
77G lubm28000.nt
77G lubm32000.nt
77G lubm36000.nt
77G lubm40000.nt
```

Generate the benchmark code in the Benchmark directory, it can be done with `./gradlew shadowJar` command. Copy the compiled file in `build/libs/Benchmark-???-all.jar` and rename it to `bench.jar`.

Then run the script `./gen_test.sh`, it'll run the experiments 10 times (or less if stopped). The profiling files will be put in `profiling-[run]/[nt file].hdt.prof`.

The profiling files can be exploited using qepSearch in the qEndpoint CLI.
