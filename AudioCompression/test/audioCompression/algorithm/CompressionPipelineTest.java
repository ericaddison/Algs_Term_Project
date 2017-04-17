package audioCompression.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

import audioCompression.types.AudioCompressionType;

public class CompressionPipelineTest {

	private static final String INITIAL_S = "unprocessed";
	private static final int INITIAL_I = 0;
	private static final double INITIAL_D = -12.34;
	private static final String PROCESSED_S = "processed";
	private static final int PROCESSED_I = 999;
	private static final double PROCESSED_D = 123.456;
	
	@Test
	public void testInstantiate() {
		CompressionPipeline pipeline = new CompressionPipeline();
		assertNotNull(pipeline);
		assertEquals(0, pipeline.getNumberOfSteps());
	}
	
	@Test
	public void testAddOneStep() {
		CompressionPipeline pipeline = new CompressionPipeline();
		pipeline.addStep(new TestAlgStep1());
		assertEquals(1, pipeline.getNumberOfSteps());
	}	
	
	@Test
	public void testAddTwoSteps() {
		CompressionPipeline pipeline = new CompressionPipeline();
		pipeline.addStep(new TestAlgStep1());
		pipeline.addStep(new TestAlgStep2());
		assertEquals(2, pipeline.getNumberOfSteps());
	}	
	
	@Test
	public void testProcessForwardOneStep(){
		CompressionPipeline pipeline = new CompressionPipeline();
		pipeline.addStep(new TestAlgStep1());
		AudioCompressionType input = new TestAudioType1();
		AudioCompressionType output = pipeline.processForward(input);
		assertTrue(output instanceof TestAudioType2);
		assertEquals(PROCESSED_S, ((TestAudioType2)output).value);
	}
	
	@Test
	public void testProcessForwardTwoSteps(){
		CompressionPipeline pipeline = new CompressionPipeline();
		pipeline.addStep(new TestAlgStep1());
		pipeline.addStep(new TestAlgStep2());
		AudioCompressionType input = new TestAudioType1();
		AudioCompressionType output = pipeline.processForward(input);
		assertTrue(output instanceof TestAudioType3);
		assertEquals(PROCESSED_D, ((TestAudioType3)output).value, 0.001);
	}
	
	@Test
	public void testProcessReverseOneStep(){
		CompressionPipeline pipeline = new CompressionPipeline();
		pipeline.addStep(new TestAlgStep1());
		AudioCompressionType input = new TestAudioType2();
		AudioCompressionType output = pipeline.processReverse(input);
		assertTrue(output instanceof TestAudioType1);
		assertEquals(PROCESSED_I, ((TestAudioType1)output).value);
	}
	
	@Test
	public void testProcessReverseTwoSteps(){
		CompressionPipeline pipeline = new CompressionPipeline();
		pipeline.addStep(new TestAlgStep1());
		pipeline.addStep(new TestAlgStep2());
		AudioCompressionType input = new TestAudioType3();
		AudioCompressionType output = pipeline.processReverse(input);
		assertTrue(output instanceof TestAudioType1);
		assertEquals(PROCESSED_I, ((TestAudioType1)output).value);
	}
	
	
//********************************************************************************
//		Private test classes
//********************************************************************************
	
	private class TestAlgStep1 implements AlgorithmStep<TestAudioType1, TestAudioType2>{

		@Override
		public TestAudioType2 forward(TestAudioType1 input) {
			TestAudioType2 output = new TestAudioType2();
			output.value = PROCESSED_S;
			return output;
		}

		@Override
		public TestAudioType1 reverse(TestAudioType2 input) {
			TestAudioType1 output = new TestAudioType1();
			output.value = PROCESSED_I;
			return output;
		}

		@Override
		public String getName() {
			return "TestAlgStep1";
		}

		@Override
		public Class<? extends AudioCompressionType> getInputClass() {
			return TestAudioType1.class;
		}

		@Override
		public Class<? extends AudioCompressionType> getOutputClass() {
			return TestAudioType2.class;
		}
		
	}
	
	private class TestAlgStep2 implements AlgorithmStep<TestAudioType2, TestAudioType3>{

		@Override
		public TestAudioType3 forward(TestAudioType2 input) {
			TestAudioType3 output = new TestAudioType3();
			output.value = PROCESSED_D;
			return output;
		}

		@Override
		public TestAudioType2 reverse(TestAudioType3 input) {
			TestAudioType2 output = new TestAudioType2();
			output.value = PROCESSED_S;
			return output;
		}

		@Override
		public String getName() {
			return "TestAlgStep2";
		}

		@Override
		public Class<? extends AudioCompressionType> getInputClass() {
			return TestAudioType2.class;
		}

		@Override
		public Class<? extends AudioCompressionType> getOutputClass() {
			return TestAudioType3.class;
		}
		
	}
	
	private class TestAudioType1 implements AudioCompressionType{
		int value = INITIAL_I;
	}
	
	private class TestAudioType2 implements AudioCompressionType{
		String value = INITIAL_S;
	}
	
	private class TestAudioType3 implements AudioCompressionType{
		double value = INITIAL_D;
	}

}
