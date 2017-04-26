package audioCompression.algorithm;

import java.awt.Color;
import java.io.File;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;
import audioCompression.ByteRestrictionAcousticModel;
import audioCompression.algorithm.dsp.CosineModulatedFilterBank;
import audioCompression.algorithm.dsp.window.BlackmannWindow;
import audioCompression.algorithm.dsp.window.HammingWindow;
import audioCompression.algorithm.dsp.window.HannWindow;
import audioCompression.algorithm.dsp.window.KaiserWindow;
import audioCompression.algorithm.dsp.window.KbdWindow;
import audioCompression.algorithm.dsp.window.RectangleWindow;
import audioCompression.algorithm.dsp.window.Window;
import audioCompression.types.AudioByteBuffer;
import audioCompression.types.AudioFile;
import audioCompression.types.CompressedAudioFile;
import audioCompression.types.RawAudio;
import audioCompression.types.Subbands;
import audioCompression.types.WavAudioInput;
import audioCompression.types.WavAudioOutput;

public class FilterBankStepDemo {
	
	private static Color[] colors = new Color[] 
			{
		Color.ORANGE, 
		Color.BLUE, 
		Color.RED, 
		Color.GREEN, 
		Color.CYAN, 
		Color.MAGENTA, 
		Color.YELLOW};
	
	public static void main(String[] args){
		// adjustable parameters
		int nbands = 8;					// number of subbands 2, 4, 8, 16, 32, 64
		int fN = 4*512;					// length of the filter-bank filter, 128, 256, 512, 1024, 2048, 4096
		int winSize = 100*nbands;		// size of signal window
		Window w = new HannWindow(fN);	// window used to construct filter
		
		// I think there might be some offset problem with too many bands (or channels)!!!
		
		// apply to audio test
		String filename = "../src_wavs/nokia_tune.wav";
		
		if(winSize%nbands>0)
			System.out.println("WARNING! winsize and overlap incompatible!");
		WavAudioInput audio = new WavAudioInput(new File(filename), winSize, 0);
		
		String inFile = "../src_wavs/nokia_tune.wav";
		String compFile = "../output_wavs/" + FilterBankStepDemo.class.getSimpleName() + ".jet";

		
		CompressionPipeline pipeline = new CompressionPipeline();
		InputOutputStep io = new InputOutputStep();
		io.setWindowLength(1024);
		
		FilterBankStep fb = new FilterBankStep(nbands, w);
		int filterLength = 16*512;
		fb.setFilterWindow(new KbdWindow(filterLength, 0.001f));
		//fb.setFilterWindow(new KaiserWindow(filterLength, 0.001f));
		//fb.setFilterWindow(new HannWindow(filterLength));
		fb.setnBands(4);
		
		String outFile = "../output_wavs/" + FilterBankStepDemo.class.getSimpleName() + "_" + fb.getFilterWindow().getClass().getSimpleName() + ".wav";
		
		SubbandsByteBufferizerStep sbb = new SubbandsByteBufferizerStep();
		sbb.setAdaptive(true);
		sbb.setPsychoAcousticModel(new ByteRestrictionAcousticModel(audio.getSampleRate(), 0, 10000));
		
		pipeline.addStep(io);
		pipeline.addStep(fb);
		pipeline.addStep(sbb);
		pipeline.addStep(new HuffmanEncoderStep());
		pipeline.addStep(new SerializationStep());

		CompressedAudioFile comp = (CompressedAudioFile)pipeline.processForward(new AudioFile(inFile), compFile);
		AudioFile finalFile = (AudioFile)pipeline.processReverse(comp, outFile);

		RawAudio output = io.getOutputWav();

		System.out.println("error: " + io.getRmsError());
		
		PlotFrame plot = new PlotFrame(new PlotPanel(1,1));
		float[] firstHalf = output.getAudioBuffer((int)audio.getNSamples()/2)[0];
		plot.getPlotPanel().addPoints(audio.getAudioBuffer((int)audio.getNSamples())[0]);
		PointsView pv = plot.getPlotPanel().addPoints(firstHalf);
		pv.setLineColor(Color.red);
		plot.setSize(500, 400);
		plot.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
		plot.setVisible(true);
		
	}
}
