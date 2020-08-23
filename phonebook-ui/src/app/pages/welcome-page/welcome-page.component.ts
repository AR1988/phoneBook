import {Component, OnDestroy, OnInit} from '@angular/core';
import {ContactService} from "../../service/contact.service";
import {Subscription} from "rxjs";
import {UserService} from "../../service/user.service";
import {User} from "../../service/interface";

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.css']
})
export class WelcomePageComponent implements OnInit, OnDestroy {
  private subscriptionGetUser: Subscription;
  private subscriptionRemove: Subscription;
  public user: User;


  constructor(public contactService: ContactService, public userService: UserService) {
  }

  ngOnInit(): void {
    this.subscriptionGetUser = this.userService.getUser().subscribe(value => this.user = value);
  }

  onClickRemove(id: number) {
    this.subscriptionRemove = this.contactService.removeContact(id);
  }

  ngOnDestroy(): void {
    if (this.subscriptionGetUser)
      this.subscriptionGetUser.unsubscribe();
    if (this.subscriptionRemove)
      this.subscriptionRemove.unsubscribe();
  }
}
