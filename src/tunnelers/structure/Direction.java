/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunnelers.structure;

/**
 *
 * @author Stepan
 */
public enum Direction {
    North(0, -1, 0, 0), NorthEast(1, -1, 1, 0),
    East(1, 0, 0, 1),   SouthEast(1, 1, 1, 1),
    South(0, 1, 0, 2),  SouthWest(-1, 1, 1, 2),
    West(-1, 0, 0, 3),  NorthWest(-1, -1, 1, 3);
    
    
    private int x, y, imgX, imgY;
    
            
            
    private Direction(int x, int y, int imgX, int imgY){
        this.x = x; this.y = y;
        this.imgX = imgX; this.imgY = imgY;
    }
    
}
