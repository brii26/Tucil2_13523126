# Tucil2_13523126

> Tugas Kecil 2 IF2211 Strategi Algoritma

# QuadTree Image Compression and GIF Generator

This project implementing Quadtree data structure to compress images and to create GIF animation which shows
image reconstruction step-by-step based on the depth of the Quadtree. Compression algorithm involves several methods of error measurement such as Variance, Mean Absolute Deviation, Max Pixel Difference, Entropy, and Strutural Similarity Index (SSIM) to divide blocks efficiently.

## Main Feature

- **Image Compression Through divide-and-conquer Algorithm**  
  Using the divide-and-conquer algorithm to compress images
  
- **Error Method Measurement**  
  There are 5 main methods implemented on this project, such as:   
  - Variance  
  - Mean Absolute Deviation (MAD)  
  - Max Pixel Difference  
  - Entropy  
  - Structural Similarity Index (SSIM)
  
- **GIF Animation**  
  Create an animation through GIF to represent the process of each level nodes progress where image were being compressed.

## Requirement

- Java Development Kit (JDK) V8.0 and above.
- Good operating system 

## How to run

Execute the file run.bat using the command line on CLI : 

```sh
./run.bat
```

## Project Structure

```
Tucil2_13523126/
├── doc/
├── bin/
├── src/
│   ├── Main.java
│   ├── QuadTree.java
│   ├── ProcessImage.java
│   ├── ErrorMeasurement.java
│   └── GifWriter.java
├── test/
├── run.bat
├── README.md
├── .gitignore
```

## Author

Brian Ricardo Tamin – 13523126
