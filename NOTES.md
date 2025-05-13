NOTE: we have an endpoint to get the watch provider
- JustWatch Attribution Required
- use http-only cookies
- do refresh tokens instead of access tokens when there is an update.. / limit the token gen to critical updates
- note, i just witnessed an issue with the AI prompt thing, it returned an empty list even with the proper prompt
  might need a way to double check the AI model response - part of the clean up/limiting
docker run -p 50001:50001 ai_model_image



THINGS TO TEST:
- all controllers
- all services


unit - core parts of methods

integration - dry run (MockMVC + RestTemplate)

https://stackoverflow.com/questions/39865596/difference-between-using-mockmvc-with-springboottest-and-using-webmvctest



- how to cache recommendation with redis?
- the problem is that 2 people can type in different sentences to get the same result, redis using key mappings can't really do that unless
  - we brute force it by stripping for common 'variables'
  - use NLP...


<!-- NOTE:
our Python server runs on HTTP/1.1, to fix this we are on the spring side referencing 1.1, but you can also get aroudn this with TLS

 -->