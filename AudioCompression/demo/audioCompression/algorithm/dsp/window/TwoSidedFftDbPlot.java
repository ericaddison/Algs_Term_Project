package audioCompression.algorithm.dsp.window;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import edu.mines.jtk.dsp.Fft;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.mosaic.GridView;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.GridView.Style;
import edu.mines.jtk.mosaic.PointsView;
import edu.mines.jtk.mosaic.PointsView.Line;

public class TwoSidedFftDbPlot extends PlotFrame{

	private static final long serialVersionUID = 1L;
	private static Color[] colors = new Color[] 
			{
		Color.BLACK, 
		Color.BLUE, 
		Color.RED, 
		Color.GREEN, 
		Color.CYAN, 
		Color.MAGENTA, 
		Color.YELLOW};

	public TwoSidedFftDbPlot(int N, float[][] data) {
		super(new PlotPanel(1,1));
		
		Sampling f = (new Fft(N)).getFrequencySampling1();
		float[] freqs = negativeTwoSideIfy(f.getValues());
		
		for(int i=0; i<data.length; i++){
			PointsView pv = 
					this.getPlotPanel().addPoints(freqs,twoSidedFftMagDb(data[i]));
			pv.setLineColor(colors[i%colors.length]);
			pv.setLineWidth(2);
			if(i%2==1)
				pv.setLineStyle(Line.DASH);
		}

		GridView gv = this.getPlotPanel().addGrid();
		gv.setColor(Color.LIGHT_GRAY);
		gv.setStyle(Style.DASH);

		float[] dummy = new float[freqs.length];
		Arrays.fill(dummy, 5f);
		
		this.getPlotPanel().addPoints(freqs, dummy);
		
	}	
	
	public TwoSidedFftDbPlot(int N, List<float[]> data) {
		super(new PlotPanel(1,1));
		
		Sampling f = (new Fft(N)).getFrequencySampling1();
		float[] freqs = negativeTwoSideIfy(f.getValues());
		
		for(float[] d : data){
			PointsView pv = 
					this.getPlotPanel().addPoints(freqs,twoSidedFftMagDb(d));
			pv.setLineColor(colors[data.indexOf(d)%colors.length]);
			pv.setLineWidth(2);
		}

		GridView gv = this.getPlotPanel().addGrid();
		gv.setColor(Color.LIGHT_GRAY);
		gv.setStyle(Style.DASH);
		
	}

	private float[] twoSideIfy(float[] oneSided){
		int N = oneSided.length;
		float[] twoSided = new float[N*2-1];
		twoSided[N/2] = oneSided[0];
		
		for(int i=0; i<N; i++){
			twoSided[i] = oneSided[N-i-1];
			twoSided[N+i-1] = oneSided[i];
		}
		
		return twoSided;
	}

	private float[] negativeTwoSideIfy(double[] oneSided){
		int N = oneSided.length;
		float[] twoSided = new float[N*2-1];
		twoSided[N/2] = (float)oneSided[0];
		
		for(int i=0; i<N; i++){
			twoSided[i] = -(float)oneSided[N-i-1];
			twoSided[N+i-1] = (float)oneSided[i];
		}
		
		return twoSided;
	}
	
	private float[] twoSidedFftMagDb(float[] arr){
		return twoSideIfy(fftMagDb(arr));
	}
	
	private float[] fftMagDb(float[] arr){
		Fft fft = new Fft(arr.length);
		float[] fftMag = complexMag(fft.applyForward(arr));
		fftMag[fftMag.length-1] = fftMag[fftMag.length-2]; 
		
		
		// find max
		float max = -Float.MAX_VALUE;
		for(int i=0; i<fftMag.length; i++)
			max = Math.max(max, Math.abs(fftMag[i]));
		
		// db relative to max
		for(int i=0; i<fftMag.length; i++){
			if(fftMag[i]==0)
				fftMag[i] = (i==0)?-100:fftMag[i-1];
			else
				fftMag[i] = 10*(float)Math.log10(Math.abs(fftMag[i])/max);
		}
		
		return fftMag;
	}
	
	private float[] complexMag(float[] cpx){
		float[] mag = new float[cpx.length/2];
		
		for(int i=0; i<mag.length; i++)
			mag[i] = (float)Math.sqrt( cpx[2*i]*cpx[2*i] + cpx[2*i+1]*cpx[2*i+1] );
		return mag;
	}
	
}
