package audioCompression.algorithm;

import java.util.Iterator;

import audioCompression.algorithm.dsp.MDCT;
import audioCompression.algorithm.dsp.window.KbdWindow;
import audioCompression.algorithm.dsp.window.Window;
import audioCompression.types.AudioCompressionType;
import audioCompression.types.Lines;
import audioCompression.types.Subbands;

public class MdctStep implements AlgorithmStep<Subbands, Lines> {

	private static final String NAME = "MDCT";
	
	@Override
	public Lines forward(Subbands input, String name) {
		
		Window w = makeWindow(input.getSamplesPerWindow()); 
		
		Iterator<float[][][]> iter = input.getWindowIterator();
		float[][][][] windows = new float[input.getNChannels()][input.getNBands()][input.getNWindows()*2+1][input.getSamplesPerWindow()/2];
		
		int winCount = 0;
		
		// enforce window overlap here
		
		float[][][] patchedWin = new float[input.getNChannels()][input.getNBands()][input.getSamplesPerWindow()];
		while(iter.hasNext()){
			float[][][] nextWin = iter.next();
			
			for(int ichan=0; ichan<input.getNChannels(); ichan++){
				for(int jband=0; jband<input.getNBands(); jband++){

					for(int i=0; i<input.getSamplesPerWindow()/2; i++){
						patchedWin[ichan][jband][i] = patchedWin[ichan][jband][i+input.getSamplesPerWindow()/2];	// shift window down
						patchedWin[ichan][jband][i+input.getSamplesPerWindow()/2] = nextWin[ichan][jband][i];		// fill second half with new data
					}
					
					float[] windowed = w.apply(patchedWin[ichan][jband]);
					windows[ichan][jband][2*winCount] = MDCT.forward(windowed);
					
					for(int i=0; i<input.getSamplesPerWindow()/2; i++){
						patchedWin[ichan][jband][i] = patchedWin[ichan][jband][i+input.getSamplesPerWindow()/2];	// shift window down
						patchedWin[ichan][jband][i+input.getSamplesPerWindow()/2] = nextWin[ichan][jband][i+input.getSamplesPerWindow()/2];		// fill second half with new data
					}
					
					windowed = w.apply(patchedWin[ichan][jband]);
					windows[ichan][jband][2*winCount+1] = MDCT.forward(windowed);
				}
			}
			winCount++;
		}
		
		// one more for symmetric zero padding
		for(int ichan=0; ichan<input.getNChannels(); ichan++){
			for(int jband=0; jband<input.getNBands(); jband++){

				for(int i=0; i<input.getSamplesPerWindow()/2; i++){
					patchedWin[ichan][jband][i] = patchedWin[ichan][jband][i+input.getSamplesPerWindow()/2];	// shift window down
					patchedWin[ichan][jband][i+input.getSamplesPerWindow()/2] = 0;		// fill second half with new data
				}
				
				float[] windowed = w.apply(patchedWin[ichan][jband]);
				windows[ichan][jband][winCount++] = MDCT.forward(windowed);
			}
		}	
				
		Lines newLines = new Lines(input.getSampleRate(), 
				input.getNWindows()*2+1, 
				input.getSamplesPerWindow()/2, 
				input.getNChannels(), 
				input.getNBands(), 
				input.getByteDepth(), 
				windows);
				
		
		return newLines;
	}

	@Override
	public Subbands reverse(Lines input, String name) {

		Window w = makeWindow(2*input.getSamplesPerWindow());
		
		Iterator<float[][][]> iter = input.getWindowIterator();
		float[][][][] windows = new float[input.getNChannels()][input.getNBands()][input.getNWindows()][input.getSamplesPerWindow()*2];
		int winCount = 0;
		int halfLength = input.getSamplesPerWindow();
		float[][][] win1;
		float[][][] win2 = iter.next();
			
		for(int ichan=0; ichan<input.getNChannels(); ichan++)
			for(int jband=0; jband<input.getNBands(); jband++)
				win2[ichan][jband] = w.apply(MDCT.reverse(win2[ichan][jband]));
		
		while(iter.hasNext()){
			win1 = iter.next();
			int offset = (winCount%2) * halfLength;
			for(int ichan=0; ichan<input.getNChannels(); ichan++){
				for(int jband=0; jband<input.getNBands(); jband++){
					win1[ichan][jband] = w.apply(MDCT.reverse(win1[ichan][jband]));
					for(int i=0; i<halfLength; i++)
						windows[ichan][jband][winCount/2][i+offset] += win1[ichan][jband][i] + win2[ichan][jband][i+halfLength];
				}
			}
			
			win2 = win1;
			winCount++;
		}		
		
		Subbands newSub = new Subbands(input.getSampleRate(), 
				input.getNWindows()/2-1, 
				input.getSamplesPerWindow()*2, 
				input.getSamplesPerWindow(),
				input.getNChannels(), 
				input.getNBands(), 
				input.getByteDepth(), 
				windows);
				
		
		return newSub;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Class<? extends AudioCompressionType> getInputClass() {
		return Subbands.class;
	}

	@Override
	public Class<? extends AudioCompressionType> getOutputClass() {
		return Lines.class;
	}
	
	private Window makeWindow(int N){
		return new KbdWindow(N, 0.05f);
	}


}
