import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { JobsListingComponent } from './components/jobs-listing.component';
import { JobDetailsComponent } from './components/job-details.component';
import { ApplicationFormComponent } from './components/application-form.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './login/signup.component';
import { ApplicantSignupComponent } from './login/applicant-signup.component';
import { BusinessSignupComponent } from './login/business-signup.component';
import { ApplicantComponent } from './components/applicant.component';
import { BusinessComponent } from './employercomp/business.component';
import { CreateJobComponent } from './employercomp/create-job.component';
import { SubmitSuccessComponent } from './components/submit-success.component';
import { SavedComponent } from './components/saved.component';
import { ListingsComponent } from './employercomp/listings.component';
import { ApplicantsComponent } from './employercomp/applicants.component';
import { AppProfileComponent } from './employercomp/app.profile.component';
import { BizProfileComponent } from './components/biz.profile.component';
import { authGuard, autologinGuard, premiumGuard, roleGuard } from './guards/guards';
import { MyProfileComponent } from './components/my-profile.component';
import { SuccessComponent } from './payment/success.component';
import { CancelComponent } from './payment/cancel.component';
import { AppSearchComponent } from './employercomp/app-search.component';
import { ActiveUsersComponent } from './employercomp/active-users.component';
import { BizPremiumComponent } from './employercomp/biz-premium.component';
import { PremSearchComponent } from './employercomp/prem-search.component';

const routes: Routes = [
  { path: '', component: LoginComponent, canActivate: [autologinGuard], title: "Login | Fused"  },
  { path: 'signup', component: SignupComponent, canActivate: [autologinGuard], title: "Signup | Fused" },
  { path: 'applicant-signup', component: ApplicantSignupComponent, title: "Signup | Fused" },
  { path: 'business-signup', component: BusinessSignupComponent, title: "Signup | Fused" },
  { path: 'premium-success', component: SuccessComponent },
  { path: 'premium-cancel', component: CancelComponent },
  {
    path: 'applicant', component: ApplicantComponent, canActivate: [authGuard, roleGuard],  title: "Home | Fused",
    data: {
      expectedRole: "APPLICANT"
    },
    children: [
      { path: 'listings', component: JobsListingComponent, title: "Home | Fused" },
      { path: 'details/:jobid', component: JobDetailsComponent, title: "Job | Fused"},
      { path: 'applied', component: SavedComponent,title: "My Jobs | Fused" },
      { path: 'apply/:jobid', component: ApplicationFormComponent,title: "Job | Fused" },
      { path: 'application/success', component: SubmitSuccessComponent,title: "Success | Fused" },
      { path: 'my-profile', component: MyProfileComponent,title: "My Profile | Fused" },
      { path: 'biz-profile/:companyname', component: BizProfileComponent },
    ]
  },
  {
    path: 'business', component: BusinessComponent, canActivate: [authGuard, roleGuard],
    data: {
      expectedRole: "BUSINESS"
    },
    children: [
      { path: 'app-profile/:userid', component: AppProfileComponent , title: "Profile | Fused"},
      {path: 'profile-search/:search', component: AppSearchComponent , title: "Search | Fused"}, 
      { path: 'new-post', component: CreateJobComponent , title: "New Job Post | Fused"},
      { path: 'all', component: ActiveUsersComponent, title: "Users | Fused"},
      { path: 'listing-performance', component: ListingsComponent , title: "My Listings | Fused"},
      { path: 'applicants/:jobid', component: ApplicantsComponent , title: "Job | Fused"},
      { path: 'premium', component: BizPremiumComponent , title: "Join Premium | Fused"},
      { path: 'prem-search', component: PremSearchComponent , title: "Advanced Search | Fused Premium", canActivate: [premiumGuard], data: {
        expectedPremium: true
      }}
    ]
  },


  { path: '**', redirectTo: '/', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
