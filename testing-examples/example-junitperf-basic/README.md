JUnitPerf basic example
========================

This project contains a basic example of JUnitPerf usage for performance testing.

Tests, including performance one, can be launched with

    mvn test

After all tests are executed, open the report.html file contained in the *target/junitperf* folder to see the performance report.

You can check the performance requirements by uncomenting line 61, which will add an sleep to the tested method to simulate some latency. 

If you run again the tests, you will see that the performance tests fails, as the expected *meanLatency* is not achieved.

Open the generated report again to see how it shows that the expected latency was not achieved by our performance test.