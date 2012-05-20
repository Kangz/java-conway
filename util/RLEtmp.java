package util;

import java.io.FileInputStream;
import java.io.IOException;

public class RLEtmp {

	private int xsize;
	private int ysize;
	private String rule;

	public int[][] cells;
	
	public RLEtmp(String file) {
		try {
            FileInputStream f = new FileInputStream(file);
            int c = f.read();

            // Skip comments
            while (c == '#') {
                while (c != '\n')
                    c = f.read();
                c = f.read();
            }

            // Read configuration line
            StringBuffer configuration = new StringBuffer();
            while (c != '\n') {
                configuration.append((char) c);
                c = f.read();
            }
            ParseConfigLine(configuration.toString());

            // Read data until exclamation point
            int x = 0;
            int y = 0;
            int repeat = 0;
            while (c != '!') {
                if (c == '\n' || c == '\r') {
                    // End of line in file are ignored
                } else if (c >= '0' && c <= '9') {
                    // If c is a digit : repeat number
                    repeat = (10 * repeat) + (c - '0');
                } else {
                    if (repeat == 0)
                        repeat = 1;
                    if (c == '$') {
                        // New line
                        y += repeat;
                        x = 0;
                        if (y >= ysize)
                            throw new IllegalArgumentException("Too many lines");
                    } else if (c == 'o') {
                        if (x + repeat > xsize)
                            throw new IllegalArgumentException(
                                    "Too many columns");
                        // On cell
                        while (repeat > 0) {
                            cells[x][y] = 1;
                            x++;
                            repeat--;
                        }
                    } else if (c == 'b') {
                        if (x + repeat > xsize)
                            throw new IllegalArgumentException(
                                    "Too many columns");
                        // Black cell
                        while (repeat > 0) {
                            cells[x][y] = 0;
                            x++;
                            repeat--;
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid character");
                    }
                    repeat = 0;
                }
                c = f.read();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid file " + file, e);
        }
	}
	
	private void ParseConfigLine(final String config) {
        xsize = 0;
        ysize = 0;
        rule = null;
        for (final String assignment : config.split(",")) {
            final String[] var = assignment.split("=", 2);
            if (var.length < 2)
                throw new IllegalArgumentException("Invalid line " + config);
            final String varname = var[0].trim().toLowerCase();
            final String varvalue = var[1].trim();
            if (varname.equals("x"))
                xsize = new Integer(varvalue);
            else if (varname.equals("y"))
                ysize = new Integer(varvalue);
            else if (varname.equals("rule"))
                rule = varvalue;
        }
        if (xsize <= 0)
            throw new IllegalArgumentException("Invalid x");
        if (ysize <= 0)
            throw new IllegalArgumentException("Invalid y");
        if (rule == null || !rule.equals("B3/S23"))
            throw new IllegalArgumentException("Invalid rule");

        // Allocate cells
        cells = new int[xsize][ysize];
        for (int x = 0; x < xsize; x++)
            for (int y = 0; y < ysize; y++)
                cells[x][y] = 0;
    }
	
}
