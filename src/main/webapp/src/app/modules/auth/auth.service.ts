import { EventEmitter, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


const restURL = '/api/v1/auth'

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  loginUser(data: any): Observable<{token: string}> {
    return this.http.post<{token: string}>(restURL+"/authenticate", data, {headers: {"Accept-Language": "en-CA"}});
  }

  registerUser(data: any): Observable<{token: string}> {
    return this.http.post<{token: string}>(restURL+"/register", data, {headers: {"Accept-Language": "en-CA"}});
  }

  onAuthResponse = new EventEmitter<{token: string}>();

  isLoggedIn(): Boolean {
    return !!sessionStorage.getItem("token")
  }

  logout() {
    sessionStorage.removeItem("token")
  }
}
