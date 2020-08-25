import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Contact} from "./contact";
import {Observable} from "rxjs";
import {UserService} from "./user.service";
import {Router} from "@angular/router";


@Injectable({
  providedIn: 'root'
})
export class ContactService {
  contacts: Observable<Contact[]>;
  private readonly contactPath = '/api/contact';


  constructor(private http: HttpClient, private router: Router) {
  }

  getAllContacts(): Observable<Contact[]> {
    if (!this.contacts) {
      this.reload();
    }
    return this.contacts;
  }

  private reload(): void {
    this.contacts = this.http.get<Contact[]>(`${this.contactPath}/all`);
  }

  removeContact(id: number) {
    return this.http.delete(`${this.contactPath}/${id}`).subscribe(() => this.reload());
  }
}
