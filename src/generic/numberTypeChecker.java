/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generic;

/**
 *
 * @author Stepan
 */
public class numberTypeChecker {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		byte n = 0;
		for(int i = 0; i < 300; i++){
			System.out.format("N=%8d\tIS=%s%n", n, Integer.toString(n & 0xFF, 16));
			
			n+=258;
		}
	}
	
}
