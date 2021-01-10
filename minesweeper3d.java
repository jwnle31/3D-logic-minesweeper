import java.awt.*;
import java.io.*;
import hsa_ufa.Console;
import java.util.Random;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;

public class minesweeper3d {

	static int length = 5;
	static int width = 5;
	static int height = 5;
	static int mines = 10;
	static int rightClick = 0;
	static int numFlag = 0;
	static int board[][][] = new int[length][width][height];
	static int reveal[][][] = new int[length][width][height];
	static int flag[][][] = new int[length][width][height];
	static int boardX[] = { 113, 295, 475, 657, 838 };
	static int boardY[] = { 135, 420, 135, 420, 135 };
	static Font mineFont = new Font("mine-sweeper", Font.PLAIN, 20);
	static Font mineFont2 = new Font("Algerian", Font.PLAIN, 20);
	static Font titleFont = new Font("mine-sweeper", Font.PLAIN, 80);
	static int counter = 0;
	static int startClick = 0;
	static int gamePhase = 0;
	static int numUnopened = 0;
	static int trigger = 0;
	static int trigger_x = 0;
	static int trigger_y = 0;
	static int trigger_z = 0;
	static int face_x = 576;
	static int face_y = 85;
	static String name = "";
	static Image minecover = Toolkit.getDefaultToolkit().getImage(minesweeper3d.class.getResource("minecover.png"));
	static Image flagcover = Toolkit.getDefaultToolkit().getImage(minesweeper3d.class.getResource("flagcover.png"));
	static Image smilecover = Toolkit.getDefaultToolkit().getImage(minesweeper3d.class.getResource("smilecover.png"));
	static Image helpcover = Toolkit.getDefaultToolkit().getImage(minesweeper3d.class.getResource("helpcover.png"));
	static Image playTriangle = Toolkit.getDefaultToolkit().getImage(minesweeper3d.class.getResource("pixelplay.png"));
	static Image trophy = Toolkit.getDefaultToolkit().getImage(minesweeper3d.class.getResource("trophy.png"));
	static Image rules = Toolkit.getDefaultToolkit().getImage(minesweeper3d.class.getResource("minerules.PNG"));
	static Image controls = Toolkit.getDefaultToolkit()
			.getImage(minesweeper3d.class.getResource("mouseinstruction.PNG"));
	static Image model = Toolkit.getDefaultToolkit().getImage(minesweeper3d.class.getResource("3dmodel.PNG"));
	static Random rand = new Random();
	static Timer timer = new Timer();
	static String[] highScores = new String[0];
	static String[] highScoresN = new String[0];
	static Console c = new Console(1200, 700, "3D Logic Minesweeper");

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub

		// Custom Fonts
		try {

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("mine-sweeper.ttf")));

		} catch (IOException | FontFormatException e) {
			// Handling exception
		}

		// Creating new files
		try {

			File myObj1 = new File("E:\\highScores.txt");
			File myObj2 = new File("E:\\highScoresN.txt");

			if (myObj1.createNewFile()) {

				System.out.println("File created: " + myObj1.getName());

			} else {

				System.out.println("File already exists.");

			}

			if (myObj2.createNewFile()) {

				System.out.println("File created: " + myObj2.getName());

			} else {

				System.out.println("File already exists.");

			}

		} catch (IOException e) {

			System.out.println("An error occurred.");
			e.printStackTrace();

		}

		// Basic Structure of the Game
		nameSet();

		while (true) {

			setUp();

			title();

			play();

			gameWinEnd();

			gameLoseEnd();

			help();

			highScores();

		}

	}

	public static void nameSet() throws InterruptedException {

		while (name.length() == 0 || name.length() > 10) { // Loops until the name length is acceptable

			c.setBackgroundColor(new Color(106, 226, 192));
			c.clear();

			c.setColor(new Color(0, 0, 0));

			c.setFont(titleFont);

			c.drawString("type your name: ", 25, 350);

			c.setFont(mineFont);

			// If nothing is entered
			if (name.length() == 0) {

				c.setColor(new Color(248, 0, 0));
				c.drawString("make sure to enter your name!!!", 25, 50);

			}

			// If the name is longer than 10
			if (name.length() >= 10) {

				c.setColor(new Color(248, 0, 0));
				c.drawString("your name is too long...", 25, 50);

			}

			c.setColor(new Color(0, 0, 0));
			c.setCursor(24, 0);
			name = c.readLine();

			if (name.length() > 0 && name.length() <= 10) { // When the name is accepted

				c.setFont(titleFont);
				c.setBackgroundColor(new Color(106, 226, 192));
				c.clear();
				c.drawString("type your name: ", 25, 350);
				c.setColor(new Color(248, 0, 0));
				c.drawString(name, 25, 500);
				Thread.sleep(5000);

			}

		}

	}

	public static void title() throws InterruptedException {

		// Animated bomb components
		c.setFont(titleFont);
		int bounceX1 = 552;
		int bounceY1 = 105;
		int bounceX2 = 552;
		int bounceY2 = 690;
		int iX1 = 3;
		int iY1 = 3;
		int iX2 = -3;
		int iY2 = -3;

		while (gamePhase == 0) {

			synchronized (c) {

				c.setBackgroundColor(new Color(106, 226, 192));
				c.clear();

				c.setColor(new Color(0, 0, 0));
				c.drawString("*", bounceX1, bounceY1);
				c.drawString("*", bounceX2, bounceY2);
				c.drawString("minesweeper 3d", 30, 310);
				c.setColor(new Color(248, 0, 0));
				c.drawString("minesweeper 3d", 25, 300);

				c.drawImage(minecover, 475, 350, 250, 250);
				c.drawImage(playTriangle, 475, 358, 250, 250);
				c.drawImage(helpcover, 775, 385, 180, 180);
				c.drawImage(minecover, 245, 385, 180, 180);
				c.drawImage(trophy, 270, 410, 130, 130);

			}

			// Logic of bouncing around the screen
			if (bounceX1 <= 0 || bounceX1 >= 1100) {

				iX1 *= -1;

			}

			if (bounceY1 <= 95 || bounceY1 >= 700) {

				iY1 *= -1;

			}

			if (bounceX2 <= 0 || bounceX2 >= 1100) {

				iX2 *= -1;

			}

			if (bounceY2 <= 95 || bounceY2 >= 700) {

				iY2 *= -1;

			}

			bounceX1 += iX1;
			bounceY1 += iY1;
			bounceX2 += iX2;
			bounceY2 += iY2;

			// Play button
			if (c.getMouseX() > 475 && c.getMouseX() < 725 && c.getMouseY() > 350 && c.getMouseY() < 600
					&& c.getMouseButton(0)) {

				gamePhase = 1;
				Thread.sleep(1000);

			}

			// Help button
			if (c.getMouseX() > 775 && c.getMouseX() < 955 && c.getMouseY() > 385 && c.getMouseY() < 565
					&& c.getMouseButton(0)) {

				gamePhase = 4;
				Thread.sleep(1000);

			}

			// High-score button
			if (c.getMouseX() > 245 && c.getMouseX() < 425 && c.getMouseY() > 385 && c.getMouseY() < 565
					&& c.getMouseButton(0)) {

				gamePhase = 5;
				Thread.sleep(1000);

			}

			Thread.sleep(5);

		}

	}

	public static void help() throws InterruptedException {

		c.setFont(mineFont);

		while (gamePhase == 4) {

			synchronized (c) {

				c.setBackgroundColor(new Color(106, 226, 192));
				c.clear();
				c.setColor(new Color(200, 200, 200));
				c.fill3DRect(50, 620, 95, 45, true);
				c.setColor(new Color(0, 0, 0));
				c.drawString("back", 55, 652);

				c.setColor(new Color(248, 0, 0));
				c.drawString("rules", 100, 100);
				c.drawImage(rules, 100, 115, 410, 180);
				c.drawString("controls", 100, 350);
				c.drawImage(controls, 100, 365, 210, 210);
				c.drawImage(model, 560, 115, 592, 460);
				c.drawString("layers", 930, 240);

				c.setColor(new Color(50, 50, 50));

				for (int i = 1; i <= 5; i++) {

					c.drawString("" + i, 900 - (9 * i), 265 + (40 * i));

					if (i == 1) {

						c.drawString("(top)", 950 - (9 * i), 265 + (40 * i));

					}

					if (i == 5) {

						c.drawString("(bottom)", 950 - (9 * i), 265 + (40 * i));

					}

				}

			}

			// Back Button
			if (c.getMouseX() > 50 && c.getMouseX() < 145 && c.getMouseY() > 620 && c.getMouseY() < 665
					&& c.getMouseButton(0)) {

				gamePhase = 0;
				Thread.sleep(1000);

			}

			Thread.sleep(50);

		}

	}

	public static void highScores() throws InterruptedException, IOException {

		c.setFont(mineFont);

		while (gamePhase == 5) {

			File inFile = new File("E:\\highScores.txt"); // Files to read in from
			File inFileN = new File("E:\\highScoresN.txt");
			Scanner initScanner = new Scanner(inFile);
			Scanner fReader = new Scanner(inFile); // Scanners used to read in the files
			Scanner fReaderN = new Scanner(inFileN);
			int numLines = 0;

			while (initScanner.hasNextLine()) {

				initScanner.nextLine();
				numLines++;

			}

			initScanner.close();

			// Making temporary arrays with the correct length
			String[] tempHS = new String[numLines];
			String[] tempHSN = new String[numLines];
			highScores = tempHS;
			highScoresN = tempHSN;

			if (numLines > 0) {

				for (int i = 0; i < numLines; i++) {

					highScores[i] = fReader.nextLine();
					highScoresN[i] = fReaderN.nextLine();

				}

				int[] highScoresAsInt = new int[highScores.length];

				// Converting high-scores to integers
				for (int i = 0; i < highScores.length; i++) {

					highScoresAsInt[i] = Integer.parseInt(highScores[i]);

				}

				int[] sortedScores = new int[highScores.length];
				String[] sortedNames = new String[highScoresN.length];

				// Copying the original array
				sortedScores = highScoresAsInt;
				sortedNames = highScoresN;

				// Sorting logic copied from Google Classroom and slightly modified
				for (int x = 1; x < sortedScores.length; x++) {

					int currentNum = sortedScores[x];
					String currentStr = sortedNames[x];

					int y = x;

					for (; y > 0 && sortedScores[y - 1] > currentNum; y--) {

						sortedScores[y] = sortedScores[y - 1];
						highScoresN[y] = highScoresN[y - 1];

					}

					// Putting everything into the right place
					sortedScores[y] = currentNum;
					sortedNames[y] = currentStr;

				}

				// Copying everything back to the original array
				for (int i = 0; i < highScoresAsInt.length; i++) {

					highScores[i] = Integer.toString(sortedScores[i]);
					highScoresN[i] = sortedNames[i];

				}

			}

			synchronized (c) {

				c.clear();

				c.setColor(new Color(200, 200, 200));
				c.fill3DRect(50, 620, 95, 45, true);
				c.setColor(new Color(0, 0, 0));
				c.drawString("back", 55, 652);

				// set up headings
				c.setColor(new Color(248, 0, 0));
				c.drawString("HIGH SCORES", 220, 80);
				c.drawLine(450, 100, 210, 100);
				c.drawLine(450, 105, 210, 105);
				c.drawImage(trophy, 550, 50, 600, 600);

				// Reducing to 10 names and high-scores
				if (numLines > 0) {

					if (numLines > 10) {

						numLines = 10;

					}

					for (int i = 0; i < numLines; i++) {

						if (i == 0) {
							c.setColor(new Color(201, 176, 55));
						} else if (i == 1) {
							c.setColor(new Color(180, 180, 180));
						} else if (i == 2) {
							c.setColor(new Color(173, 138, 86));
						} else {
							c.setColor(new Color(51, 165, 51));
						}

						c.drawString((i + 1) + ". " + highScoresN[i], 190, 170 + (50 * i));
						c.drawString(highScores[i], 460, 170 + (50 * i));

					}

				}

			}

			if (c.getMouseX() > 50 && c.getMouseX() < 145 && c.getMouseY() > 620 && c.getMouseY() < 665
					&& c.getMouseButton(0)) {

				// Closing the file readers before exiting the high-score screen
				fReader.close();
				fReaderN.close();

				gamePhase = 0;
				Thread.sleep(1000);

			}

			Thread.sleep(50);

		}

	}

	public static void scoresave() throws IOException {

		// Declaring files with paths
		File outFile = new File("E:\\highScores.txt");
		File outFileN = new File("E:\\highScoresN.txt");

		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, true));
		BufferedWriter writerN = new BufferedWriter(new FileWriter(outFileN, true));

		// Writes down high-scores and corresponding names
		writer.write("" + counter);
		writer.newLine();
		writer.close();

		writerN.write(name);
		writerN.newLine();
		writerN.close();

	}

	public static void setUp() throws InterruptedException {

		length = 5;
		width = 5;
		height = 5;
		mines = 10;
		rightClick = 0;
		numFlag = 0;
		counter = 0;
		startClick = 0;
		trigger = 0;
		trigger_x = 0;
		trigger_y = 0;
		trigger_z = 0;
		timer = new Timer();

		c.setFont(mineFont);

		// Default Board Values
		for (int i = 0; i < length; i++) {

			for (int j = 0; j < width; j++) {

				for (int k = 0; k < height; k++) {

					board[i][j][k] = 0;
					reveal[i][j][k] = 0;
					flag[i][j][k] = 0;

				}

			}

		}

		c.enableMouse();
		c.enableMouseMotion();

	}

	public static void play() throws InterruptedException {

		c.setFont(mineFont);

		while (gamePhase == 1) {

			synchronized (c) {

				// Restarting Game
				if (c.getMouseX() > face_x && c.getMouseX() < face_x + 45 && c.getMouseY() > face_y
						&& c.getMouseY() < face_y + 45 && c.getMouseButton(0) && c.getMouseClick() >= 1) {

					timer.cancel();
					timer.purge();
					gamePhase = 1;
					break;

				}
				// Back Button
				if (c.getMouseX() > 50 && c.getMouseX() < 145 && c.getMouseY() > 620 && c.getMouseY() < 665
						&& c.getMouseButton(0)) {

					// Stops Timer
					timer.cancel();
					timer.purge();
					gamePhase = 0;
					Thread.sleep(1000);
					break;

				}

				checkRevealFlag();

				drawPlayScreen();

				highlight();

			}

			endDetection();

			Thread.sleep(50);

		}

	}

	public static void cellAssign(int i, int j, int k) throws InterruptedException {

		// Assigning Mines to Cells
		int count = 0;
		int mine_x = 0;
		int mine_y = 0;
		int mine_z = 0;

		while (count < mines) {

			mine_x = rand.nextInt(length);
			mine_y = rand.nextInt(width);
			mine_z = rand.nextInt(height);

			if (board[mine_x][mine_y][mine_z] != 27 && !((mine_x <= i + 1 && mine_x >= i - 1)
					&& (mine_y <= j + 1 && mine_y >= j - 1) && (mine_z <= k + 1 && mine_z >= k - 1))) {

				board[mine_x][mine_y][mine_z] = 27; // 27 = Bomb
				count++;

			}

		}

		// Assigning Numbers to Cells
		for (int l = 0; l < length; l++) {

			for (int m = 0; m < width; m++) {

				for (int n = 0; n < width; n++) {

					if (board[l][m][n] == 27) {

						for (int o = -1; o <= 1; o++) {

							for (int p = -1; p <= 1; p++) {

								for (int q = -1; q <= 1; q++) {

									try {

										if (board[l + o][m + p][n + q] != 27) {

											board[l + o][m + p][n + q] += 1;

										}

									}

									catch (Exception e) {

									}

								}

							}

						}

					}

				}

			}

		}

	}

	public static void drawPlayScreen() throws InterruptedException {

		c.setBackgroundColor(new Color(106, 226, 192));
		c.clear();

		c.setColor(new Color(248, 0, 0));
		c.drawString("minesweeper 3d", 453, 60);

		c.drawImage(smilecover, face_x, face_y, 45, 45);

		c.setColor(new Color(0, 0, 0));
		c.fill3DRect(175 + 300, 50 + 35, 95, 45, true);
		c.fill3DRect(525 + 100, 50 + 35, 95, 45, true);

		// # of Bombs Left to Find
		c.setColor(new Color(248, 0, 0));
		c.drawString("" + Math.abs((mines - numFlag) % 10), 239 + 300, 82 + 35);
		c.drawString("" + Math.abs(((mines - numFlag) % 100 - (mines - numFlag) % 10) / 10), 214 + 300, 82 + 35);

		// Elapsed Time Playing the Game
		if (counter >= 999) {

			c.drawString("9", 589 + 100, 82 + 35);
			c.drawString("9", 564 + 100, 82 + 35);
			c.drawString("9", 539 + 100, 82 + 35);

		} else {

			c.drawString("" + counter % 10, 589 + 100, 82 + 35);
			c.drawString("" + (counter % 100 - counter % 10) / 10, 564 + 100, 82 + 35);
			c.drawString("" + (counter % 1000 - (counter % 100 - counter % 10)) / 100, 539 + 100, 82 + 35);

		}

		c.setColor(new Color(255, 255, 0));
		for (int i = 0; i <= 4; i++) {

			c.drawString("" + (i + 1), boardX[i] - 40, boardY[i] + 130);

		}

		c.setColor(new Color(200, 200, 200));
		c.fill3DRect(50, 620, 95, 45, true);
		c.setColor(new Color(0, 0, 0));
		c.drawString("back", 55, 652);

		for (int i = 0; i < length; i++) {

			for (int j = 0; j < width; j++) {

				for (int k = 0; k < height; k++) {

					c.setColor(new Color(200, 200, 200));
					c.fill3DRect(i * 50 + boardX[k], j * 50 + boardY[k], 45, 45, true);

					// Assigning Colours to Numbers
					if (board[i][j][k] == 1) {
						c.setColor(new Color(57, 59, 228));
					} else if (board[i][j][k] == 2) {
						c.setColor(new Color(33, 183, 33));
					} else if (board[i][j][k] == 3) {
						c.setColor(new Color(255, 0, 0));
					} else if (board[i][j][k] == 4) {
						c.setColor(new Color(0, 0, 128));
					} else if (board[i][j][k] == 5) {
						c.setColor(new Color(128, 0, 0));
					} else if (board[i][j][k] == 6) {
						c.setColor(new Color(0, 126, 128));
					} else if (board[i][j][k] == 7) {
						c.setColor(new Color(128, 0, 128));
					} else if (board[i][j][k] == 8) {
						c.setColor(new Color(114, 114, 114));
					} else if (board[i][j][k] == 9) {
						c.setColor(new Color(110, 117, 146));
					} else if (board[i][j][k] >= 10) {
						c.setColor(new Color(40, 41, 41));
					} else if (board[i][j][k] == 27) {
						c.setColor(new Color(0, 0, 0));
					}

					if (board[i][j][k] == 27) {

						c.drawString("*", (i * 50) + boardX[k] + 10, (j * 50) + boardY[k] + 33);

					} else if (board[i][j][k] < 27 && board[i][j][k] >= 20) {

						c.drawString("" + board[i][j][k], (i * 50) + boardX[k] + 2, (j * 50) + boardY[k] + 32);

					} else if (board[i][j][k] < 20 && board[i][j][k] >= 10) {

						c.drawString("" + board[i][j][k], (i * 50) + boardX[k] + 4, (j * 50) + boardY[k] + 32);

					} else {

						c.drawString("" + board[i][j][k], (i * 50) + boardX[k] + 13, (j * 50) + boardY[k] + 32);

					}

					// Covering Cells That are Not Revealed or Flagged
					if (reveal[i][j][k] == 0) {

						c.drawImage(minecover, (i * 50) + boardX[k], (j * 50) + boardY[k], 45, 45);

						if (flag[i][j][k] == 1) {

							c.drawImage(flagcover, (i * 50) + boardX[k], (j * 50) + boardY[k], 45, 45);

						}

					}

				}

			}

		}

	}

	// Checking for Adjacent Blocks Continuously
	public static void adjacentCheck(int i, int j, int k) throws InterruptedException {

		if (reveal[i][j][k] == 0 && flag[i][j][k] == 0) {

			reveal[i][j][k] = 1;

			if (board[i][j][k] == 0 && flag[i][j][k] == 0) {

				// Checking 3x3x3 block
				for (int l = -1; l <= 1; l++) {

					for (int m = -1; m <= 1; m++) {

						for (int n = -1; n <= 1; n++) {

							try {

								if (board[i + l][j + m][k + n] != 0 && board[i + l][j + m][k + n] != 27
										&& flag[i + l][j + m][k + n] == 0) {

									reveal[i + l][j + m][k + n] = 1;

								} else if (board[i + l][j + m][k + n] == 0 && flag[i + l][j + m][k + n] == 0) {

									adjacentCheck(i + l, j + m, k + n);

								}

							}

							catch (Exception e) {

							}

						}

					}

				}

			}

		}

	}

	public static void flag(int i, int j, int k) throws InterruptedException {

		if (reveal[i][j][k] == 0) {

			if (flag[i][j][k] == 0 && numFlag < mines) { // Flag

				flag[i][j][k] = 1;
				numFlag++;

			} else if (flag[i][j][k] == 1) { // Deleting flag

				flag[i][j][k] = 0;
				numFlag--;

			}

		}

	}

	public static void checkRevealFlag() throws InterruptedException {

		for (int i = 0; i < length; i++) {

			for (int j = 0; j < width; j++) {

				for (int k = 0; k < height; k++) {

					// Revealing the Cell
					if (c.getMouseX() > (i * 50) + boardX[k] && c.getMouseX() < (i * 50) + boardX[k] + 45
							&& c.getMouseY() > (j * 50) + boardY[k] && c.getMouseY() < (j * 50) + boardY[k] + 45
							&& c.getMouseButton(0) && !c.getMouseButton(2) && flag[i][j][k] == 0) {

						startClick++;

						// Timer starts as soon as the player left clicks for the first time
						if (startClick == 1) {

							cellAssign(i, j, k);

							timer.scheduleAtFixedRate(new TimerTask() {

								public void run() {

									counter++;

								}

							}, new Date(), 1000);

							startClick = 2;

						}

						adjacentCheck(i, j, k);

					}

					// Flagging the Cell
					if (c.getMouseX() > (i * 50) + boardX[k] && c.getMouseX() < (i * 50) + boardX[k] + 45
							&& c.getMouseY() > (j * 50) + boardY[k] && c.getMouseY() < (j * 50) + boardY[k] + 45
							&& c.getMouseButton(2) && c.getMouseClick() >= 1 && !c.getMouseButton(0)
							&& reveal[i][j][k] == 0) {

						flag(i, j, k);

					}

				}

			}

		}

	}

	public static void highlight() throws InterruptedException {

		for (int i = 0; i < length; i++) {

			for (int j = 0; j < width; j++) {

				for (int k = 0; k < height; k++) {

					if (c.getMouseX() > (i * 50) + boardX[k] && c.getMouseX() < (i * 50) + boardX[k] + 45
							&& c.getMouseY() > (j * 50) + boardY[k] && c.getMouseY() < (j * 50) + boardY[k] + 45) {

						c.setColor(new Color(225, 225, 0, 100));

						// Highlights the cell around the block (3x3x3)
						for (int l = -1; l <= 1; l++) {

							for (int m = -1; m <= 1; m++) {

								for (int n = -1; n <= 1; n++) {

									if (i + l >= 0 && i + l <= 4 && j + m >= 0 && j + m <= 4) {

										try {

											c.fillRect((i + l) * 50 + boardX[k + n], (j + m) * 50 + boardY[k + n], 45,
													45);

										} catch (Exception e) {

										}

									}

								}

							}

						}

					}

				}

			}

		}

	}

	public static void endDetection() throws InterruptedException {

		// Checks for # of Unopened Cells
		numUnopened = 0;

		for (int i = 0; i < length; i++) {

			for (int j = 0; j < width; j++) {

				for (int k = 0; k < height; k++) {

					if (reveal[i][j][k] == 0) {

						numUnopened++;

					}

				}

			}

		}

		if (numUnopened == mines) {

			gamePhase = 2;

		}

		// Reveals All Mines When Player Reveals a Mine
		for (int i = 0; i < length; i++) {

			for (int j = 0; j < width; j++) {

				for (int k = 0; k < height; k++) {

					if (reveal[i][j][k] == 1 && board[i][j][k] == 27) {

						trigger++;

						if (trigger == 1) {

							gamePhase = 3;

							// Saves the Position of the Clicked Mine
							trigger_x = i;
							trigger_y = j;
							trigger_z = k;

							for (int l = 0; l < length; l++) {

								for (int m = 0; m < width; m++) {

									for (int n = 0; n < height; n++) {

										if (board[l][m][n] == 27) {

											reveal[l][m][n] = 1;

										}

									}

								}

							}

						}

					}

				}

			}

		}

	}

	public static void gameWinEnd() throws InterruptedException, IOException {

		Image coolcover = Toolkit.getDefaultToolkit().getImage(minesweeper3d.class.getResource("coolcover.png"));

		if (gamePhase == 2) {

			scoresave();

			c.setColor(new Color(0, 0, 0));
			c.fill3DRect(175 + 300, 50 + 35, 95, 45, true);
			c.setColor(new Color(248, 0, 0));
			c.setColor(new Color(248, 0, 0));
			c.drawString("0", 239 + 300, 82 + 35);
			c.drawString("0", 214 + 300, 82 + 35);

			// Stops Timer
			timer.cancel();
			timer.purge();
			c.drawImage(coolcover, face_x, face_y, 45, 45);

			for (int i = 0; i < length; i++) {

				for (int j = 0; j < width; j++) {

					for (int k = 0; k < width; k++) {

						if (reveal[i][j][k] == 0) {

							// Flags Remaining Mine Cells
							c.drawImage(flagcover, (i * 50) + boardX[k], (j * 50) + boardY[k], 45, 45);

						}

					}

				}

			}

			// Game Elapsed Time
			c.setColor(new Color(244, 198, 112, 200));
			c.fillRect(0, 300, 1200, 110);
			c.setColor(new Color(0, 0, 0));
			c.drawString("Congratulations!!!", 405, 350);
			c.drawString("You finished in " + counter + " sec.", 400, 375);

		}

		while (gamePhase == 2) {

			// Restarts Game
			if (c.getMouseX() > face_x && c.getMouseX() < face_x + 45 && c.getMouseY() > face_y
					&& c.getMouseY() < face_y + 45 && c.getMouseButton(0)) {

				gamePhase = 1;

			}

			// Back button
			if (c.getMouseX() > 50 && c.getMouseX() < 145 && c.getMouseY() > 620 && c.getMouseY() < 665
					&& c.getMouseButton(0)) {

				gamePhase = 0;
				Thread.sleep(1000);

			}

			Thread.sleep(50);

		}

	}

	public static void gameLoseEnd() throws InterruptedException {

		Image deadcover = Toolkit.getDefaultToolkit().getImage(minesweeper3d.class.getResource("deadcover.png"));
		Image xcover = Toolkit.getDefaultToolkit().getImage(minesweeper3d.class.getResource("xcover.png"));

		if (gamePhase == 3) {

			checkRevealFlag();

			drawPlayScreen();

			// Stops Timer
			timer.cancel();
			timer.purge();

			// Red Tile Background on Clicked Mine
			c.setColor(new Color(248, 0, 0));
			c.fill3DRect(trigger_x * 50 + boardX[trigger_z], trigger_y * 50 + boardY[trigger_z], 45, 45, true);
			c.setColor(new Color(0, 0, 0));
			c.drawString("*", (trigger_x * 50) + boardX[trigger_z] + 10, (trigger_y * 50) + boardY[trigger_z] + 33);

			c.drawImage(deadcover, face_x, face_y, 45, 45);

			for (int i = 0; i < length; i++) {

				for (int j = 0; j < width; j++) {

					for (int k = 0; k < width; k++) {

						if (board[i][j][k] != 27 && flag[i][j][k] == 1) {

							// Draw X's on the No-Mine Cells that are Flagged
							c.setColor(new Color(200, 200, 200));
							c.fill3DRect((i * 50) + boardX[k], (j * 50) + boardY[k], 45, 45, true);
							c.setColor(new Color(0, 0, 0));
							c.drawString("*", (i * 50) + boardX[k] + 10, (j * 50) + boardY[k] + 33);
							c.drawImage(xcover, (i * 50) + boardX[k], (j * 50) + boardY[k], 45, 45);

						}

					}

				}

			}

		}

		while (gamePhase == 3) {

			// Restarts Game
			if (c.getMouseX() > face_x && c.getMouseX() < face_x + 45 && c.getMouseY() > face_y
					&& c.getMouseY() < face_y + 45 && c.getMouseButton(0)) {

				gamePhase = 1;

			}

			// Back button
			if (c.getMouseX() > 50 && c.getMouseX() < 145 && c.getMouseY() > 620 && c.getMouseY() < 665
					&& c.getMouseButton(0)) {

				gamePhase = 0;
				Thread.sleep(1000);

			}

			Thread.sleep(50);

		}

	}

}
