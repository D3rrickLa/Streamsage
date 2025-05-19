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


 Summary:
Address Remaining Issues: Fix any bugs and review code quality.

Testing: Complete integration testing and consider increasing coverage.

Code Review: Self-review and request peer reviews.

Performance & Security: Optional performance tests and security audits.

Documentation: Document your code and potential future improvements.

Deploy: Deploy your app and check configurations.

Monitoring: Set up monitoring/logging for production.

Feedback: Gather feedback from users and improve the product.




        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (tokenService.decrypt(token) != null) {
                System.out.println("user exists");
                session.setAttribute("suggestionPackage", suggestionPackage);             
            }
        }
        don't think we need this becaue the check is already there is suggestionPackage is empty
        if empty we know no user has been logged in


  NOTE: we can't have the postgres and redis bundled into the docker compose file
  becasue we are going to be using cloud providers for these things, need to keep
  it modular 



  we do have something for our redis to do this format: movie:amoutn
  - when we ask the ai to gen N movies, it gives us N in a list - mighit not be accurate but still is a number
  - we can use that to append the thing and the current code does get use our movie...



  Core Components
Prompt Component (prompt.component.ts) – Users enter prompts to get AI-based movie recommendations.

Movie Recommendations Component (recommendations.component.ts) – Displays AI-generated movie suggestions.

Authentication Components
Login Component (auth/login.component.ts) – Handles user login.

Signup Component (auth/signup.component.ts) – Allows new users to register.

Auth Service (auth/auth.service.ts) – Manages authentication logic (JWT, API calls, etc.).

User Profile & Feedback Components
Profile Component (users/profile.component.ts) – Enables users to view and edit their details.

Feedback Component (users/feedback.component.ts) – Displays and allows submission of feedback.

Admin Dashboard (if you choose to implement it)
Admin Component (admin/admin-dashboard.component.ts) – Manages system-wide data, reviews feedback, and monitors AI performance.

UI Components
Navbar Component (shared/navbar.component.ts) – Navigation bar for login, profile, and prompt sections.

Footer Component (shared/footer.component.ts) – Displays site info.

Loading Indicator Component (shared/loading.component.ts) – Shows a spinner while AI generates recommendations.

Services
Movie Service (services/movie.service.ts) – Handles API requests for movie recommendations.

User Service (services/user.service.ts) – Manages user data and interactions.