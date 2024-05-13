// 207404997
/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 */

/*
 * Most of the functions in this class are simple set/get/init functions.
 * the 3 complex ones are allDistance, fill, and shortestPath.
 * fill and shortestPath both work by calling allDistance and using the returned array as data.
 */

public class Map implements Map2D {
	private int[][] _map;
	private boolean _cyclicFlag;

	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {init(w,h, v);}

	/**
	 * Constructs a square map (size*size).
	 * @param size
	 */
	public Map(int size) {this(size,size, 0);}

	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}

	@Override
	/*
	initializes map of given width & height, and value v.
	creates the array attribute of the map according to given parameters:
	 */
	public void init(int w, int h, int v) {
		// set sizes of array:
		this._map = new int[w][h];
		// iterate over the 2D array, set every index to value v:
		for(int i=0; i<w; i++){
			for(int j=0; j<h; j++){
				this._map[i][j] = v;
			}
		}
	}

	@Override
	/**
	 * @param arr 2D array
	 * initializes map which corresponds to the given 2D array.
	 * @throws RuntimeException if arr == null or if the array is empty or a ragged 2D array.
	 */
	public void init(int[][] arr) {
		// check if ragged:
		boolean isRagged = false;
		int a = 0;
		while(!isRagged && a<arr.length){
			isRagged = arr[a].length!=arr[0].length;
			a++;
		}
		// check that array isn't ragged, empty, or null:
		if (isRagged || arr.length==0){
			throw new RuntimeException("ragged array");
		}
		// initialize array for map, same size as given array:
		this._map = new int[arr.length][arr[0].length];
		// set values of map array according to given array:
		for(int i=0; i< arr.length; i++){
			System.arraycopy(arr[i], 0, this._map[i], 0, arr[0].length);
		}
	}

	@Override
	/*
	 * returns a copy of the 2D array representing the map:
	 */
	public int[][] getMap() {
		int[][] ans;
		// set ans size according to map:
		ans = new int[this._map.length][this._map[0].length];
		// set ans values according to map:
		for(int i=0; i<ans.length; i++){
			System.arraycopy(this._map[i], 0, ans[i], 0, ans[0].length);
		}
		return ans;
	}

	@Override
	/*
	 * returns the width of the array representing the map:
	 */
	public int getWidth() {
		return this._map.length;
	}

	@Override
	/*
	 * returns the height of the array representing the map:
	 */
	public int getHeight() {
		return this._map[0].length;
	}

	@Override
	/*
	 * returns the value of the pixel at the coordinates [x][y]:
	 */
	public int getPixel(int x, int y) {
		return this._map[x][y];
	}

	@Override
	/*
	 * returns the value of the pixel p:
	 */
	public int getPixel(Pixel2D p) {
		return this.getPixel(p.getX(),p.getY());
	}

	@Override
	/*
	 * sets the value of the pixel at coordinates [x][y] to v:
	 */
	public void setPixel(int x, int y, int v) {
		this._map[x][y] = v;
	}

	@Override
	/*
	 * sets the value of the pixel to v:
	 */
	public void setPixel(Pixel2D p, int v) {
		this._map[p.getX()][p.getY()] = v;
	}

	@Override
	/**
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	/*
	Calls allDistance on the map with start point 'xy'.
	For every index on the 'allDistance' map that is a positive number,
	(meaning is reachable from 'xy'), change value to 'new_v'
	Return the amount of pixels that were changed (including the source).
	 */
	public int fill(Pixel2D xy, int new_v) {
		int ans=0;
		// get the allDistance map:
		int[][] map = this.allDistance(xy, -2).getMap();
		// iterate over map and set relevant pixels to 'new_v':
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[0].length; j++){
				if(map[i][j]>=0){
					this.setPixel(i, j, new_v);
					// add 1 to the count:
					ans++;
				}
			}
		}
		return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	/*
	Calls the allDistance function on the map.
	The last place in the ans array - the last step - is set to p2.
	Then, iterating 'backwards' in ans, check the 4 neighbors (north-east-south-west)
	and if the neighbor pixel has a value one less than p2, add it to the array.
	Since we're going "backwards" from the end point,
	the distance is guaranteed to be the shortest possible.
	Then, set p2 to be the neighbor that we found. repeat until p1 is reached.
	Default return value is 'null', if there is no path.
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
		Pixel2D[] ans = null;
		// if p1==p2, distance is 0; otherwise, check that p1 & p2 are inside map:
		if(p1.equals(p2)){
			ans = new Pixel2D[] { p1 };
		}
		// if one of the pixels isn't inside the map, will return the default value null:
		else if(this.isInside(p1) && this.isInside(p2)){
			// get the allDistance map, set its cyclic value to be the same as original:
			Map2D distanceMap= this.allDistance(p1,obsColor);
			distanceMap.setCyclic(this.isCyclic());
			// if p2 is at a point with value '-1', meaning it's unreachable:
			if(distanceMap.getPixel(p2)<0){
				// will return the default 'null':
				return ans;
			}
			// set ans length to the length of the shortest path:
			int length = distanceMap.getPixel(p2);
			ans = new Pixel2D[length+1];
			// set last place as p2:
			ans[length] = new Index2D(p2);
			// for each step, use 'nextPixel' to find the next step:
			for (int i=length-1; i>=0 ; i--) {
				ans[i] = new Index2D(nextPixel(distanceMap,p2));
				// set p2 as the current step, for next iteration:
				p2 = new Index2D(nextPixel(distanceMap,p2));
			}
		}
		return ans;
	}

	/*
	returns a neighbor pixel that has value of p minus one.
	this function is complimentary to shortestPath;
	returns the pixel that should be the next step.
	checks the pixel's neighbors and if it finds a relevant one, returns it.
	after a relevant neighbor is found, there is no need to check the other neighbors.
	 */
	public static Pixel2D nextPixel(Map2D map, Pixel2D p){
		int[][] mapArr = map.getMap();
		// checks that p is in map:
		if(map.isInside(p) && map.getPixel(p)>=0){
			int x = p.getX(), y = p.getY(), w = mapArr.length, h = mapArr[0].length, v = map.getPixel(p);
			// if map is set to cyclic, check the places that are relevant:
			if(map.isCyclic()){
				if(x==0 && mapArr[w-1][y]==v-1){
					return new Index2D(w-1, y);
				}
				if(y==0 && mapArr[x][h-1]==v-1){
					return new Index2D(x,h-1);
				}
				if(x==w-1 && mapArr[0][y]==v-1){
					return new Index2D(0,y);
				}
				if(y==h-1 && mapArr[x][0]==v-1) {
					return new Index2D(x, 0);
				}
			}
			// check the pixel's neighbors:
			if(x>0 && mapArr[x-1][y]==v-1){
				return new Index2D(x-1, y);
			}
			if(y>0 && mapArr[x][y-1]==v-1){
				return new Index2D(x, y-1);
			}
			if(x<w-1 && mapArr[x+1][y]==v-1){
				return new Index2D(x+1, y);
			}
			if(y<h-1 && mapArr[x][y+1]==v-1){
				return new Index2D(x, y+1);
			}
		}
		// if no neighbor was found, return (-2, -2) as default:
		return new Index2D(-2, -2);
	}

	@Override
	/*
	 * returns true if p is inside the map, false if outside.
	 * checks if x, y values are larger than zero and smaller than the width and height of the map:
	 */
	public boolean isInside(Pixel2D p) {
		boolean isInside = true;
		if(p.getX() > this._map.length || p.getX() < 0 ||
				p.getY() > this._map[0].length || p.getY() < 0){
			isInside = false;
		}
		return isInside;
	}

	@Override
	/*
	 * returns the map is set to cyclic - true pr false:
	 */
	public boolean isCyclic() {
		return this._cyclicFlag;
	}

	@Override
	/*
	 * sets the cyclic value of the map according to cy:
	 */
	public void setCyclic(boolean cy) {
		this._cyclicFlag = cy;
	}

	public void setNeighbors(Pixel2D p, int v){
		int x = p.getX(), y = p.getY(), w = this.getWidth(), h = this.getHeight();
		Pixel2D p1;
		if(this._cyclicFlag){
			if(x==0){
				p1 = new Index2D(w-1, y);
				this.setPixel(p1, v);
			}
			if(x==w-1){
				p1 = new Index2D(0, y);
				this.setPixel(p1, v);
			}
			if(y==0){
				p1 = new Index2D(x, h-1);
				this.setPixel(p1, v);
			}
			if(y==h-1){
				p1 = new Index2D(x, 0);
				this.setPixel(p1, v);
			}
		}
		if(x>0){
			p1 = new Index2D(x-1, y);
			this.setPixel(p1, v);
		}
		if(x<w-1){
			p1 = new Index2D(w-1, y);
			this.setPixel(p1, v);
		}
		if(y>0){
			p1 = new Index2D(x, y-1);
			this.setPixel(p1, v);
		}
		if(y<h-1){
			p1 = new Index2D(x, h-1);
			this.setPixel(p1, v);
		}
	}

	@Override
	/*
	 * creates an int[][] array, a copy of 'this._map'
	 * sets obstacles to -2, every other point to -1
	 * sets the pixel at the start point to zero
	 * r (radius) is set to 1.
	 * If map is cyclic, iterates over entire array.
	 * If not cyclic, iterate over pixels in radius 'r' of start point -
	 * each time will check a larger radius. (to avoid iterating over entire array when not necessary)
	 * For every pixel, if it's value is a natural number -
	 * meaning it's reachable from the start point -
	 * change its relevant neighbors to the current radius.
	 * a neighbor is relevant if it's in the array and not an obstacle.
	 * When changing the neighbors, also set 'go' to true.
	 * advance 'r' by 1.
	 * this repeats as long as 'go' is true. Every repetition starts by setting 'go' to false,
	 * and only changing it back to true when a pixel is changed.
	 * so the loop stops if a full iteration goes by with no change.
	 */
	public Map2D allDistance(Pixel2D start, int obsColor) {
		Map2D ans;
		int r = 1, w = this.getWidth(), h = this.getHeight();
		int[][] map = this.getMap();
		// set obstacles to -2. all other points to -1:
		for(int i=0; i<w; i++){
			for(int j=0; j<h; j++){
				if(map[i][j]==obsColor){
					map[i][j] = -2;
				}
				else{
					map[i][j] = -1;
				}
			}
		}
		boolean go = true;
		// set start point to zero:
		int x = start.getX(), y = start.getY();
		map[x][y] = 0;
		while(go){
			go = false;
			if(this.isCyclic()){
				// iterates over entire array:
				for(int i=0; i<w; i++){
					for(int j=0; j<h; j++){
						// if finds a pixel with value of r-1;
						// set relevant neighbors to 'r':
						if(map[i][j]==r-1) {
							if (i == 0 && map[w - 1][j] == -1) {
								map[w - 1][j] = r;
								go = true;
							}
							if (j == 0 && map[i][h - 1] == -1) {
								map[i][h - 1] = r;
								go = true;
							}
							if (i == w - 1 && map[0][j] == -1) {
								map[0][j] = r;
								go = true;
							}
							if (j == h - 1 && map[i][0] == -1) {
								map[i][0] = r;
								go = true;
							}
							if (i > 0 && map[i - 1][j] == -1) {
								map[i - 1][j] = r;
								go = true;
							}
							if (j > 0 && map[i][j - 1] == -1) {
								map[i][j - 1] = r;
								go = true;
							}
							if (i < w - 1 && map[i + 1][j] == -1) {
								map[i + 1][j] = r;
								go = true;
							}
							if (j < h - 1 && map[i][j + 1] == -1) {
								map[i][j + 1] = r;
								go = true;
							}
						}
					}
				}
			}
			else{
				// if not cyclic, check the pixels in radius 'r' of start:
				for(int i=x-r; i<x+r; i++){
					for(int j=y-r; j<y+r; j++){
						// if finds a pixel with value of r-1;
						// set relevant neighbors to 'r';
						if(i>=0 && i<w && j>=0 && j<h && map[i][j]==r-1) {
							if (i > 0 && map[i - 1][j] == -1) {
								map[i - 1][j] = r;
								go = true;
							}
							if (j > 0 && map[i][j - 1] == -1) {
								map[i][j - 1] = r;
								go = true;
							}
							if (i < w - 1 && map[i + 1][j] == -1) {
								map[i + 1][j] = r;
								go = true;
							}
							if (j < h - 1 && map[i][j + 1] == -1) {
								map[i][j + 1] = r;
								go = true;
							}
						}
					}
				}
			}
			// advance 'r' for next round:
			r++;
		}
		// create map with the array we made:
		ans = new Map(map);
		return ans;
	}
}
