import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import {} from 'googlemaps';

@Component({
  selector: 'sparkles-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit, AfterViewInit {

  @ViewChild('map', { static: true }) mapElement: any;

  map: google.maps.Map;

  constructor() { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    const mapProperties = {
      center: new google.maps.LatLng(35.2271, -80.8431),
      zoom: 15,
      mapTypeId: google.maps.MapTypeId.ROADMAP
   };

   this.map = new google.maps.Map(this.mapElement.nativeElement,    mapProperties);
  }
}
