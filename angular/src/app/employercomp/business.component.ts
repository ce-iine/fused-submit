import { Component, Inject, OnInit, inject } from '@angular/core';
import { Business, SideNavRoute } from '../models';
import { ListingService } from '../service/listing.service';
import { UserService } from '../service/user.service';
import { ViewService } from '../service/view.service';
import { BizJobService } from '../service/biz.job.service';
import { Subscribable, Subscription } from 'rxjs';
import { NotifService } from '../notif.service';

@Component({
  selector: 'app-business',
  templateUrl: './business.component.html',
  styleUrl: './business.component.css'
})
export class BusinessComponent implements OnInit{
  
  sideNavRoutes: SideNavRoute[] = [
    {route: 'listing-performance', label: 'My Listings', matIconName: 'home'},
    {route: 'new-post', label: 'Create Post', matIconName: 'work'},
    {route: 'all', label: 'All Users', matIconName: 'work'},
  ]
  
  private bizJobSvc = inject(BizJobService)
  notification!: boolean
  sub!: Subscription
  private listingSvc = inject(ListingService)

  private notifSvc = inject(NotifService)
  
  business!: Business
  
  ngOnInit(): void {
    let user = UserService.getUser();
    this.business = {id: user['id'],company_name: user['company_name'],company_email: user['company_email'], premium:user['premium']}
    this.getnotification()
    this.getBizInfo() 

    this.notifSvc.data$.subscribe((data) => {
      // console.log("BIZ COMP RECEIVED", data )
      this.getnotification()
    });

  }
  
   getnotification(){
    this.sub = this.bizJobSvc.fetchUnreadNotifications(this.business).subscribe(notifications => {
      this.notification = notifications.newNotifications 

      this.sideNavRoutes.forEach(route => {
        if (route.label === 'My Listings') {
          route.notification = this.notification; 
        }
      });
    });
  }

  getBizInfo(){ 
    this.listingSvc.getJobByCompanyName(this.business.company_name).then( res =>{
     let logourl = res.company_logo

     this.business.logo = logourl
    let user = UserService.getUser();

    this.business = {id: user['id'],company_name: user['company_name'],company_email: user['company_email'], logo:logourl, premium:user['premium']}
    UserService.saveUser(this.business)


    }).catch(err => console.log(err))
  }
  
}