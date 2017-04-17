package audioCompression.types.testImpls;

import audioCompression.types.Subbands;

public class SubbandsImpl extends Subbands {

	public SubbandsImpl(int sampsPerWindow) {
		super(100, 10, sampsPerWindow, sampsPerWindow/2, 1, 1, 2, null);
		this.windows = new float[1][2][10][sampsPerWindow]; 
		
		int cnt=0;
		for(int i=0; i<2; i++)
			for(int j=0; j<10; j++)
				for(int k=0; k<sampsPerWindow; k++)
					windows[0][i][j][k] = 1+cnt++;
	}

}
