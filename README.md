# Algs_Term_Project
Term project for the UT Austin ECE course "Algorithmic Foundation for Software Systems"

The goal of this project was to implement, modify, and examine a system of algorithmic components. We chose to explore audio compression as a familiar and relevant example of such a system. The primary goal of audio compression depends on the application. An audio compressor can optimize for various qualities, for instance:
- decompressed sound qualty
- compression ratio
- compression time
- decompression time.

Frequently, pushing the system to perform well in one of these areas will cause it to suffer in another.

This repository contains our implementation of an MP3-like audio compressor. The processing steps include:
- signal windowing
- polyphase filterbank
- Byte bufferizer with Psycho-acoustic model
- Huffman encoding
- Serialization

## Code
The Java code in this repo is organized as follows:
- AudioCompression/src - main operational classes
- AudioCompression/demo - demo files and main test-bench
- AudioCompression/test - small, incomplete set of tests

Particular packages/classes of interest include:
- AudioCompression/demo/general/FullCompressionDemo.java - the main test-bench class
- AudioCompression/src/audioCompression/algorithm - all of the algorithmic implementations
- AudioCompression/src/audioCompression/compressors - the actual compressor classes
- AudioCompression/src/audioCompression/types - custom types used in the compression pipeline
