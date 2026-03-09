Purpose: Detailed API reference.
Content to include:
- Upload API → endpoint, method, parameters, sample request/response.
## Upload Example
```bash
curl -X POST http://localhost:8080/upload \
  -F "file=@sample.txt"

Document uploaded successfully \\ response


- Query API → request body, explanation of topN, sample output.
## Query Example 
Based on the query asked it will generate the response. //refer examples.md for clear undersatnding. query is retrieval 


- Ask API → request body, sample answer, notes on how topN affects results.
## Ask Example
Based on the question asked it will give the detailed explanation. ask endpoint is retrieval+generation


- Error handling (file too large, empty file, malformed JSON).
File too large → 413 Payload Too Large (or custom message)
Empty file - success with 0 chunks
Malformed JSON - 400 Bad request
