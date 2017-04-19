package audioCompression.types;

import java.io.File;

import audioCompression.algorithm.FilterBankStep;
import audioCompression.algorithm.SubbandsByteBufferizerStep;
import audioCompression.algorithm.dsp.window.BartlettWindow;
import audioCompression.algorithm.dsp.window.BlackmannWindow;
import audioCompression.algorithm.dsp.window.HammingWindow;
import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.algorithm.dsp.window.KaiserWindow;
import audioCompression.algorithm.dsp.window.KbdWindow;
import audioCompression.algorithm.dsp.window.RectangleWindow;

public class WavAudioOutputDemo {

	
	public static void main(String[] args){
			
			int nbands = 8;
			int fN = 2*512;
			
			// I think there might be some offset problem with too many bands (or channels)!!!
			
			// apply to audio test
			//RawAudioImpl audio = new RawAudioImpl(1000, 40*nbands, 00, 1);
			String filename = "../src_wavs/nokia_tune.wav";
			
			int winSize = 200*nbands;
			int winOverlap = 0;
			
			if(winSize%nbands>0)
				System.out.println("WARNING! winsize and overlap incompatible!");
			WavAudioInput audio = new WavAudioInput(new File(filename), winSize, winOverlap);
			//FilterBankStep fb = new FilterBankStep(nbands, new HannWindow(fN));
			//FilterBankStep fb = new FilterBankStep(nbands, new HammingWindow(fN));
			//FilterBankStep fb = new FilterBankStep(nbands, new KaiserWindow(fN,0.05f));
			//FilterBankStep fb = new FilterBankStep(nbands, new BartlettWindow(fN));
			FilterBankStep fb = new FilterBankStep(nbands, new BlackmannWindow(fN));
			//FilterBankStep fb = new FilterBankStep(nbands, new RectangleWindow(fN));
			//FilterBankStep fb = new FilterBankStep(nbands, new KbdWindow(fN, 0.01f));
			SubbandsByteBufferizerStep sbb = new SubbandsByteBufferizerStep();
			
			String fileName = "testName";
			
			Subbands sub = fb.forward(audio, fileName);
			AudioByteBuffer abb = sbb.forward(sub, fileName);
			Subbands sub2 = sbb.reverse(abb, fileName);
			WavAudioOutput output = (WavAudioOutput)fb.reverse(sub2, fileName);
			
			output.writeFile("../output_wavs/WavAudioOutputDemo.wav");
			
			
	}
	
}
