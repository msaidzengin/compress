# Compress

This is BIL 211 Computer Programming I Homework 3, completed on 18 January 2019.

The program compresses a text file by giving each distinct character a fixed-width binary code, packing those bits into bytes, and writing a binary file. It can read that file back and restore the text.

## Run

```bash
javac src/Compress.java
java -cp src Compress
```

At the prompt:

- `C` compresses a file and writes `<name>.S`
- `D` decompresses a `.S` file and writes the name with the last two characters removed
- `Q` quits

The assignment handout is `bil211spring2018hw3.pdf`.
