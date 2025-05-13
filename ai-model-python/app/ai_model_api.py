import asyncio
from time import sleep
from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
import uvicorn
from app.routers import ai_response
from app.util.rate_limit import limiter
from slowapi import _rate_limit_exceeded_handler
from slowapi.middleware import SlowAPIMiddleware
from slowapi.errors import RateLimitExceeded

app = FastAPI()

app.state.limiter = limiter
app.include_router(ai_response.ai_response_router)
app.add_middleware(SlowAPIMiddleware)
app.add_exception_handler(RateLimitExceeded, _rate_limit_exceeded_handler)

@app.middleware("http")
async def log_headers(request: Request, call_next):
    # print(dict(request.headers))
    return await call_next(request)

async def main() -> None:
    try:
        config = uvicorn.Config("ai_model_api:app", host="localhost", port=50001, reload=True)
        server = uvicorn.Server(config=config)
        print("Starting Server")
        await server.serve()  # Start server
    except KeyboardInterrupt:
        print("Server stopping by user")
    except Exception as e:
        print(f"Error in main loop: {e}")
    finally:
        print("Shutting down server...")
        await server.shutdown()  # Ensure the proper shutdown call
        await sleep(1)  # Small delay to allow clean exit

if __name__ == "__main__":
    asyncio.run(main())