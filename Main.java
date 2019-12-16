import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;

public class Main {
	public static image_data readimage(String filepath) {
		File file = new File(filepath);
		BufferedImage image;
		int width, height;
		try {
			image = ImageIO.read(file);
			width = image.getWidth();
			height = image.getHeight();
			int[][] pixels = new int[width][height];
			int rgp;
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					rgp = image.getRGB(i, j);
					pixels[i][j] = rgp & 0xffffff;
					System.out.print(pixels[i][j] + " ");
				}
				System.out.println();
			}
			image_data imagedata = new image_data(width, height, pixels);

			return imagedata;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}

	}

	public static int[][] incrementvalue(int w, int h, int[][] v) {
		int[][] res = new int[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				res[i][j] = v[i][j] + 1;
			}
		}
		return res;
	}

	public static int[][] decrementvalue(int w, int h, int[][] v) {
		int[][] res = new int[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				res[i][j] = v[i][j] - 1;
			}
		}
		return res;
	}

	public static void code(image_data imagedata, int width, int height, int codebook) {

		// Create Array Of Vectors
		int[][][] vectors = new int[imagedata.width / width * imagedata.height / height][width][height];

		// Divide into Vectors and fill The Array Of Vectors
		int count = 0;
		for (int i = 0; i < imagedata.width; i += width) {
			for (int j = 0; j < imagedata.width; j += height) {
				for (int a = 0; a < width; a++) {
					for (int b = 0; b < height; b++) {
						// System.out.println(i+" "+j);
						vectors[count][a][b] = imagedata.pixels[i + a][j + b];
					}
				}
				count++;
			}
		}

		System.out.println("\n\n\n" + count);
		int[][] tempvector = new int[width][height];
		for (int c = 0; c < count; c++) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					tempvector[i][j] += vectors[c][i][j];
					// System.out.print(vectors[c][i][j] + " ");
				}
			}
			// System.out.println("\n");
		}

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tempvector[i][j] /= count;
				// System.out.print(tempvector[i][j] + " ");
			}
		}
		// System.out.println();
		ArrayList<vector> Splitvectors = new ArrayList<vector>();
		Splitvectors.add(new vector(width, height, tempvector));
		int splitecount = 1;
		while (true) {
			for (int i = 0; i < splitecount; i++) {
				int[][] v = incrementvalue(width, height, Splitvectors.get(i).v);
				Splitvectors.add(new vector(width, height, v));
				v = decrementvalue(width, height, Splitvectors.get(i).v);
				Splitvectors.add(new vector(width, height, v));
			}

			// System.out.println(Splitvectors.size());
			int sp = Splitvectors.size();
			for (int i = 0; i < sp / 3; i++) {
				Splitvectors.remove(0);
			}
			int[][][][] avragevector = new int[Splitvectors.size()][100000][width][height];

			for (int c = 0; c < count; c++) {
				int[] value = new int[Splitvectors.size()];
				int minindex = 0;
				int max = 100000;
				int count2=0;
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						for (int j2 = 0; j2 < Splitvectors.size(); j2++) {
							value[j2] += Math.pow(vectors[c][i][j] - Splitvectors.get(j2).v[i][j], 2);
						}
					}
				}
				for (int i = 0; i < Splitvectors.size(); i++) {
					value[i] = (int) Math.sqrt(value[i]);
					if (value[i] < max) {
						minindex = i;
						max = value[i];
					}
					// System.out.print(value[i] + " ");
				}
//				System.out.println();
				
//				for (int i = 0; i < width; i++) {
//					for (int j = 0; j < height; j++) {
//						avragevector[minindex][count][i][j]=vectors[c][i][j];
//					}
//				}

			}
//			for (int i = 0; i < Splitvectors.size(); i++) {
//				Splitvectors.get(i).dispaly();
//			}
			System.out.println();
			splitecount *= 2;
			if (Splitvectors.size() >= codebook) {
				break;
			}
		}

	}

//	27 28 
//	35 36 
//	28 29 
//	36 37 
//	26 27 
//	34 35 
//	28 29 
//	36 37 
//	26 27 
//	34 35 
//	29 30 
//	37 38 
//	27 28 
//	35 36 
	public static void main(String[] args) {
		// image_data imagedata = readimage("1.jpg");
		int[][] w = new int[8][8];
		int c = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				w[i][j] = c;
				System.out.print(w[i][j] + " ");
				c++;
			}
			System.out.println();

		}

		image_data d = new image_data(8, 8, w);
		code(d, 2, 2, 8);
	}
}