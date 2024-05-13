// 207404997
import java.awt.*;
import java.util.Arrays;
import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

public class Ex3Algo implements PacManAlgo {
	private int _count;
	// during developing, used to get info for debugging:
	private static final boolean DEBUG = false;
	public static boolean eatAroundGhostBox=false;
	public Ex3Algo() {_count=0;}
	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return "This algorithm first eats the food around the ghost box, and then works with the following steps:" +
				"\n1. checks which ghosts are active and finds distance to each ghost." +
				"If a ghost is closer than 5 steps, this activates the \"ghostIsClose\" case." +
				"\n2. Within the \"ghostIsClose\" case, check how much time is left to eat the ghost: " +
				"\n2a. If it's less than 1 second, then the ghost is a threat." +
				"Calculate the point that is furthest from the dangerous ghosts" +
				"(only the ones within range 5) and return that point as the target." +
				"\n2b. If there is more than 1 second, then return that ghost's position as the target." +
				"\n3. If no ghosts are close, find the closet food and return that point as the target.";
	}

	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
		int code = 0;
		int blue = Game.getIntColor(Color.BLUE, code);
		int pink = Game.getIntColor(Color.PINK, code);
		int black = Game.getIntColor(Color.BLACK, code);
		int green = Game.getIntColor(Color.GREEN, code);
		int[][] board = game.getGame(0);
		String pos = game.getPos(code);
		GhostCL[] ghosts = game.getGhosts(code);
		if(_count==0 || _count==300) {
			printBoard(board);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			System.out.println("Pacman coordinate: "+pos);
			printGhosts(ghosts);
		}
		_count++;
		// get copy of game map:
		Map2D map = new Map(board);
		map.setCyclic(game.isCyclic());
		// first, eat the points around the ghost's home:
		if(eatAroundGhostBox){
			if(map.getPixel(15,14)==pink){
				return Game.RIGHT;
			}
			if(map.getPixel(15,9)==pink){
				return Game.DOWN;
			}
			if(map.getPixel(7,9)==pink){
				return Game.LEFT;
			}
			if(map.getPixel(7,14)==pink){
				return Game.UP;
			}
			if(map.getPixel(7,14)!=pink){
				eatAroundGhostBox=false;
			}
		}
		// get player position:
		Pixel2D pacPos = playerPosition(pos);
		// get ghost info:
		int[][] ghostsInfo = ghostInfo(ghosts);
		// get the next target pixel:
		Pixel2D target = target(board, pacPos, ghostsInfo, blue, pink, green, game.isCyclic());
		// check which ghosts are active and have less than 2 seconds to be eaten:
		for(int i=0; i<ghosts.length; i++){
			if(ghosts[i].getStatus()==1 && ghosts[i].remainTimeAsEatable(0)<1){
				// calculate shortestPath from the ghost to player position:
				Pixel2D ghostPos = new Index2D(ghostsInfo[i][2], ghostsInfo[i][3]);
				Pixel2D[] path = map.shortestPath(ghostPos, pacPos, blue);
				// set the first 2 pixels of the path -
				// i.e. the ghosts position and the pixel close to the player - as obstacles;
				// this ensures that the player's path will avoid them.
				if(path!=null && path.length>1){
					map.setPixel(path[0], blue); map.setPixel(path[1], blue);
				}
			}
		}
		// calculate the shortest path from current location to target:
		Pixel2D[] path = map.shortestPath(pacPos, target, blue);
		if(DEBUG){System.out.println("path: "+ Arrays.toString(path));}
		// nextDir will calculate the appropriate int (representing a direction)
//		if(path==null){
//			return -1;
//		}
		return nextDir(path, map.getWidth(), map.getHeight());
	}

	/*
	General:
	Checks if ghosts are active and within 5 steps of the pacman:
	If the ghosts are edible, return one of them as the target. (returns the first one it finds)
	If the ghosts aren't edible, find the spot farthest from all the ghosts
	(largest accumulated distance from every one) and return that spot as the target.

	If no ghosts are within 5 steps, finds the closest food and returns that pixel as the target.
	 */
	/**
	 * decides the next target. this is the "brain" of the algorithm.
	 * @param mapArr int[][] representing the current map
	 * @param pacPos Pixel2D, current player position
	 * @param ghosts int[][] containing the ghosts current data
	 * @param obsColor int representing obstacle color
	 * @param food int representing food color
	 * @param power int representing powerup color
	 * @param cyclic boolean, is the map set to cyclic
	 * @return the next pixel that to player should aim to get to.
	 */
	public static Pixel2D target(int[][] mapArr, Pixel2D pacPos, int[][] ghosts, int obsColor, int food, int power, boolean cyclic){
		Pixel2D target = new Index2D(pacPos);
		int w = mapArr.length, h = mapArr[0].length;
		// create map from given array
		Map map = new Map(mapArr);
		map.setCyclic(cyclic);
		boolean ghostIsClose = false;
		// accumulated distance map:
		int[][] ghostDistance = new int[w][h];
		// for each ghost: checks if ghosts are active:
		for(int i=0; i<ghosts.length; i++){
			if (ghosts[i][0] == 1) {
				// finds shortest path from ghost to pacman:
				Pixel2D ghostPos = new Index2D(ghosts[i][2], ghosts[i][3]);
				Pixel2D[] path = map.shortestPath(ghostPos, pacPos, obsColor);
				// if path is less than 5 steps:
				if(path.length<5){
					ghostIsClose = true;
					// if ghost isn't eatable:
					if(ghosts[i][4]<1 || (GameInfo.CASE_SCENARIO==4 && ghosts[i][4]<2)){
						// add the distance from the ghost to the accumulated distance map:
						int[][] temp = map.allDistance(ghostPos, obsColor).getMap();
						for(int x=0; x<w; x++){
							for(int y=0; y<h; y++){
								ghostDistance[x][y] += temp[x][y];
							}
						}
					}
					// if ghost is edible, return its location as the target:
					else {
						target = new Index2D(ghostPos);
						if(DEBUG){ System.out.println("hunting: "+target); }
						return target;
					}
				}
			}
		}
		// if a ghost is close and not eatable;
		// find the pixel with the largest accumulated distance from all the ghosts;
		if(ghostIsClose){
			for(int x=0; x<w; x++){
				for(int y=0; y<h; y++){
					if(ghostDistance[x][y]>ghostDistance[target.getX()][target.getY()]){
						target = new Index2D(x, y);
					}
				}
			}
			if(DEBUG){ System.out.println("escape: "+target); }
			// return it as the target:
			return target;
		}

		// if no ghosts are relevant, continue with default objective: eat all the food:
		// do allDistance on the new map
		Map2D distanceMap = map.allDistance(pacPos, obsColor);
		distanceMap.setCyclic(cyclic);
		// find a pixel with food and set it as the target:
		boolean stop = false;
		for(int i=0; i<distanceMap.getWidth() && !stop; i++){
			for(int j=0; j<distanceMap.getHeight() && !stop; j++){
				if(map.getPixel(i, j) == food || map.getPixel(i, j) == power){
					target = new Index2D(i, j);
					stop = true;
				}
			}
		}
		// find the closest food: iterate over map and if a pixel has food and is closer to pacman, set it as the target:
		int distance = distanceMap.getPixel(target), newDistance;
		for(int i=0; i<distanceMap.getWidth(); i++){
			for(int j=0; j<distanceMap.getHeight(); j++){
				Pixel2D p = new Index2D(i, j);
				newDistance = distanceMap.getPixel(i, j);
				if((map.getPixel(i, j)==food || map.getPixel(i, j)==power) && newDistance<distance){
					distance = newDistance;
					target = new Index2D(i, j);
				}
			}
		}
		if(DEBUG){ System.out.println("food: "+target); }
		// return the closest pixel that has food:
		return target;
	}

	/**
	 * returns a pixel of the pacman current position
	 * @param s from "game.getPos()", pacman position
	 * @return Pixel2D of pacman position
	 */
	public static Pixel2D playerPosition(String s){
		Pixel2D pacPos = null;
		String[] info = s.split(",");
		// check if the input string is only ints, to avoid an error of trying to parse int from string.
		if(isOnlyInts(info) && info.length==2){
			int x = Integer.parseInt(info[0]), y = Integer.parseInt(info[1]);
			pacPos = new Index2D(x, y);
		}
		return pacPos;
	}

	/**
	 * checks that a string[] is all ints.
	 * @param s an array of strings
	 * @return whether every string of the array is all ints or not.
	 */
	public static boolean isOnlyInts(String[] s){
		boolean isOnlyInts = true;
		for(int i=0; i<s.length; i++){
			if (!s[i].matches("[0-9]+")) {
				isOnlyInts = false;
				break;
			}
		}
		return isOnlyInts;
	}

	/**
	 * returns a 2D array of the ghost's info;
	 * every row is a ghost. the info in order of:
	 * status, type, x position, y position, eatable time left
	 * @param gs array of ghosts
	 * @return int[][] the ghosts info
	 */
	public static int[][] ghostInfo(GhostCL[] gs){
		// each ghost: [status][type][x][y][time]
		int[][] ans = new int[gs.length][5];
		String[] ghostPos;
		for(int i=0;i< ans.length-1;i++){
			GhostCL g = gs[i];
			ans[i][0] = g.getStatus();
			ans[i][1] = g.getType();
			ghostPos = g.getPos(0).split(",");
			if (isOnlyInts(ghostPos)) {
				ans[i][2] = Integer.parseInt(ghostPos[0]);
				ans[i][3] = Integer.parseInt(ghostPos[1]);
			}
			// cast to int; will round down:
			ans[i][4] = (int)g.remainTimeAsEatable(0);
		}
		return ans;
	}

	/**
	 * returns an int representing the next direction for the pacman;
	 * according to the relation between the current position and the next pixel.
	 * current position is p[0], next pixel is p[1]
	 * @param p array of pixels from shortestPath()
	 * @return int[1-4], representing a direction in the game.
	 */
	public static int nextDir(Pixel2D[] p, int w, int h){
		if(p!=null && p.length>=2){
			if(p[0].getX()==0 && p[1].getX()==w-1){
				if(DEBUG){ System.out.println("cyclic left"); }
				return Game.LEFT;
			}
			if(p[0].getX()==w-1 && p[1].getX()==0){
				if(DEBUG){ System.out.println("cyclic right"); }
				return Game.RIGHT;
			}
			if(p[0].getY()==0 && p[1].getY()==h-1){
				if(DEBUG){ System.out.println("cyclic down"); }
				return Game.DOWN;
			}
			if(p[0].getY()==h-1 && p[1].getY()==0){
				if(DEBUG){ System.out.println("cyclic up"); }
				return Game.UP;
			}
			if(p[0].getX() == p[1].getX()-1){
				if(DEBUG){ System.out.println("right"); }
				return Game.RIGHT;
			}
			if(p[0].getX() == p[1].getX()+1){
				if(DEBUG){ System.out.println("left"); }
				return Game.LEFT;
			}
			if(p[0].getY() == p[1].getY()-1){
				if(DEBUG){ System.out.println("up"); }
				return Game.UP;
			}
		}
		// default return value:
		if(DEBUG){ System.out.println("down"); }
		return Game.DOWN;
	}

	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}

	public static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}

	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}
}