import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';


const restURL = '/api/v1/accounts'


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {}


  getUserInfo(): Observable<User> {
    return this.http.get<User>(restURL+"/profile");
  }

  updateUserInfo(data: any): Observable<{token: string}> {
    return this.http.put<{token: string}>(restURL+"/profile", data);
  }

  updateUserPassword(data: any) {
    return this.http.put(restURL + "/password", data, { responseType: 'text' });
  }

  onUserResponse = new EventEmitter<{token: string}>();
}
