package com.sujatha.docqa.model;

public class EmbeddingResponse {

    private Result result = new Result();

    public Result getResult() {
        return result;
    }

    public static class Result {
        public float[] getOutput() {
            // Dummy embedding array
            return new float[]{0.0f, 0.0f};
        }
    }
}