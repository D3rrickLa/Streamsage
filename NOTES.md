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




colors
- #793BE6 -> main color
- #C31DE0 -> gradient to 
- #AA88E6 -> accent/secondary color
- #423CE6 -> Background color

- #37343C -> corp color 1
- #564E66 -> corp color 2



 "CA": {
      "link": "https://www.themoviedb.org/movie/744276-after-ever-happy/watch?locale=CA",
      "rent": [
        {
          "logo_path": "/9ghgSC0MA082EL6HLCW3GalykFD.jpg",
          "provider_id": 2,
          "provider_name": "Apple TV",
          "display_priority": 6
        },
        {
          "logo_path": "/8z7rC8uIDaTM91X0ZfkRf04ydj2.jpg",
          "provider_id": 3,
          "provider_name": "Google Play Movies",
          "display_priority": 8
        },
        {
          "logo_path": "/d1mUAhpJpxy0YMjwVOZ4lxAAbeT.jpg",
          "provider_id": 140,
          "provider_name": "Cineplex",
          "display_priority": 19
        },
        {
          "logo_path": "/pTnn5JwWr4p3pG8H6VrpiQo7Vs0.jpg",
          "provider_id": 192,
          "provider_name": "YouTube",
          "display_priority": 36
        },
        {
          "logo_path": "/seGSXajazLMCKGB5hnRCidtjay1.jpg",
          "provider_id": 10,
          "provider_name": "Amazon Video",
          "display_priority": 59
        }
      ],
      "free": [
        {
          "logo_path": "/j7D006Uy3UWwZ6G0xH6BMgIWTzH.jpg",
          "provider_id": 212,
          "provider_name": "Hoopla",
          "display_priority": 10
        }
      ],
      "flatrate": [
        {
          "logo_path": "/pbpMk2JmcoNnQwx5JGpXngfoWtp.jpg",
          "provider_id": 8,
          "provider_name": "Netflix",
          "display_priority": 0
        },
        {
          "logo_path": "/pvske1MyAoymrs5bguRfVqYiM9a.jpg",
          "provider_id": 119,
          "provider_name": "Amazon Prime Video",
          "display_priority": 2
        },
        {
          "logo_path": "/fbveJTcro9Xw2KuPIIoPPePHiwy.jpg",
          "provider_id": 701,
          "provider_name": "FilmBox+",
          "display_priority": 88
        },
        {
          "logo_path": "/dpR8r13zWDeUR0QkzWidrdMxa56.jpg",
          "provider_id": 1796,
          "provider_name": "Netflix Standard with Ads",
          "display_priority": 109
        },
        {
          "logo_path": "/8aBqoNeGGr0oSA85iopgNZUOTOc.jpg",
          "provider_id": 2100,
          "provider_name": "Amazon Prime Video with Ads",
          "display_priority": 148
        }
      ],
      "buy": [
        {
          "logo_path": "/9ghgSC0MA082EL6HLCW3GalykFD.jpg",
          "provider_id": 2,
          "provider_name": "Apple TV",
          "display_priority": 6
        },
        {
          "logo_path": "/8z7rC8uIDaTM91X0ZfkRf04ydj2.jpg",
          "provider_id": 3,
          "provider_name": "Google Play Movies",
          "display_priority": 8
        },
        {
          "logo_path": "/d1mUAhpJpxy0YMjwVOZ4lxAAbeT.jpg",
          "provider_id": 140,
          "provider_name": "Cineplex",
          "display_priority": 19
        },
        {
          "logo_path": "/pTnn5JwWr4p3pG8H6VrpiQo7Vs0.jpg",
          "provider_id": 192,
          "provider_name": "YouTube",
          "display_priority": 36
        },
        {
          "logo_path": "/seGSXajazLMCKGB5hnRCidtjay1.jpg",
          "provider_id": 10,
          "provider_name": "Amazon Video",
          "display_priority": 59
        }
      ]
    },