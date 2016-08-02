package funny.gobang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadHelper {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public int[] readNextDown(String chessMan){
		System.out.println("Please input next "+chessMan +" down X,Y :");
		String line = null;
		try {
			line = br.readLine();
			System.out.println("readline:" + line);
		} catch (IOException e) {
			throw new RuntimeException("System.in read error!");
		}
		
		if (line == null || line.trim().equals("q") 
				|| line.trim().equals("bye") || line.trim().equals("exit")
				|| line.trim().equals("quit")){
			return null;
		}
		String[] down = line.split(",");
		
		if (down.length != 2){
			return new int[]{-1,-1};
		}
		int x = -1; 
		int y = -1;
		try{
			x = Integer.valueOf(down[0]);
			y = Integer.valueOf(down[1]);
		}catch(Exception e){
			String str = x == -1 ? down[0] : down[1];
			System.out.println("Can't covert "+ str +" to number" );
		}
		return new int[]{x,y};
	}
}
