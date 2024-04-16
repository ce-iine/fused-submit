import { Component, Input, OnInit, inject } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { SideNavRoute } from '../models';

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrl: './side-nav.component.css'
})
export class SideNavComponent implements OnInit{

  router = inject(Router)

  @Input() 
  sideNavRoutes: SideNavRoute[] = [] 

  isMenuExpanded: boolean = true

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        event.urlAfterRedirects.split('/').pop()
      }
    })
  }

  toggleMenu() {
    this.isMenuExpanded = !this.isMenuExpanded;
  }

}
