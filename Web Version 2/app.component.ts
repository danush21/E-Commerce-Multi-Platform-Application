import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import * as $ from 'jquery';
import { ElementRef, ViewChild } from '@angular/core';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {


  productSearchForm: FormGroup;
  ebayData: any;
  postalcodeData: any;
  productinfoData: any;
  photosData: any;
  similaritemsData: any;
  fromTextValue: any;

  p: number = 1;

  selectedConditions: any;

  itemsPerPage: number = 10;
  totalItems: number = 50;
  items: any[] = [];
  wishlistStates: boolean[] = [];


  detailsButtonEnabled: boolean = false;
  showResultsTable: boolean = true;
  showDetailsTable: boolean = false;


  constructor(private http: HttpClient, private fb: FormBuilder) {
    this.productSearchForm = this.fb.group({
      keyword: ['', [Validators.required, keywordValidator]],
      category: ['all'],
      conditionNew: false,
      conditionUsed: false,
      conditionUnspecified: false,
      localPickup: false,
      freeShipping: false,
      distance: [10, Validators.required],
      from: 'currloc',
      fromText: [{ value: '', disabled: true }, [Validators.required, keywordValidator, Validators.pattern(/^\d{5}$/)]],
    });
    

    const formData = this.productSearchForm.value;

  }


  fetchUserLocation(){
    const apiUrl = `https://ipinfo.io/json?token=bf92d7450e3306`; // Replace with your API URL
    this.http.get(apiUrl).subscribe(
      (data) => {
        this.fromTextValue=data;
        this.fromTextValue=this.fromTextValue.postal;
        console.log(this.fromTextValue);
        return this.fromTextValue;
      },
      (error) => {
        console.error('API Error:', error);
      }
    );
  }

  addItem(item: any) {
    this.items.push(item);
    this.wishlistStates.push(false);
  }

  getSerialNumber(index: number): number {
    return (this.p - 1) * this.itemsPerPage + index + 1;
  }

  toggleWishlistState(index: number) {
    this.wishlistStates[index] = !this.wishlistStates[index];
  }

  isWishlistItem(index: number): boolean {
    return this.wishlistStates[index];
  }
  

  generateConditionList(): string[] {
    const formData = this.productSearchForm.value;
    const selectedConditions = [];
  
    if (formData.conditionNew) {
      selectedConditions.push('New');
    }
  
    if (formData.conditionUsed) {
      selectedConditions.push('Used');
    }
  
    if (formData.conditionUnspecified) {
      selectedConditions.push('Unspecified');
    }
  
    return selectedConditions;
  }
  

  ngOnInit() {
    ($('[data-toggle="tooltip"]') as any).tooltip();
  }
  

  onSubmit() {
    if (this.productSearchForm.valid) {
      const formData = this.productSearchForm.value;
      this.selectedConditions = this.generateConditionList();
      if(formData.from=='otherloc'){
        this.fromTextValue = this.productSearchForm.get('fromText')?.value;
      }
      else{
        // this.fetchUserLocation();
        this.fromTextValue=90037;
      }

        let apiUrl = `/api/ebaydata?keyword=${formData.keyword}&category=${formData.category}&distance=${formData.distance}&from=${this.fromTextValue}&condition=${this.selectedConditions}`;

        this.http.get(apiUrl).subscribe(
          (response) => {
            this.ebayData = response;
          },
          (error) => {
            console.error('API Error:', error);
          }
        );
    }
  }

  fetchPostalCode() {
    this.http.get('/api/postalcode').subscribe(
      (data: any) => {
        this.postalcodeData = data;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  showProductDetailsTable() {

    this.showResultsTable = false;
    this.showDetailsTable = true;
  }

  goBackToResults() {
    this.showDetailsTable = false;
    this.showResultsTable = true;
  }

  showResults(){
    this.showResultsTable = true;
  }

  showWishList(){
    this.showResultsTable = false;
  }

  currentSection: string = 'product';

  showSection(section: string) {
    this.currentSection = section;
  }

  public productTitle: string = '';
  public productImage: any[] = [];
  public productPrice: number = 0;
  public productLocation: string = '';
  public productReturnsAccepted: string = '';
  public productReturnsWithin: string = '';
  public productItemSpecifics: any;
  
  public isImageModalOpen: boolean = false;
  public productImages: string[] = [];
  
  openImageModal() {
    console.log('Opening modal');
    this.isImageModalOpen = true;
  }
  
  closeImageModal() {
    console.log('Closing modal');
    this.isImageModalOpen = false;
  }

  openImageInNewTab(imageUrl: string) {
    window.open(imageUrl, '_blank');
  }

  fetchProductInfo(item: any) {

    const itemId = item.itemId;

    let apiUrl = `/api/productinfo?itemid=${itemId}`;

    this.http.get(apiUrl).subscribe(
      (data) => {
        this.productinfoData = data;
        this.detailsButtonEnabled = true;
        this.productTitle = this.productinfoData.Item.Title;
        this.productImage = this.productinfoData.Item.PictureURL;
        this.productPrice = this.productinfoData.Item.CurrentPrice.Value;
        this.productLocation = this.productinfoData.Item.Location;
        this.productReturnsAccepted = this.productinfoData.Item.ReturnPolicy.ReturnsAccepted;
        this.productReturnsWithin = this.productinfoData.Item.ReturnPolicy.ReturnsWithin;
        this.productItemSpecifics = this.productinfoData.Item.ItemSpecifics.NameValueList;

        this.showProductDetailsTable();
        this.fetchPhotos();
        this.fetchSimilarItems(itemId);
      },
      (error) => {
        console.error(error);
      }
    );
  }

  photos1: string='';
  photos2: string='';
  photos3: string='';
  photos4: string='';
  photos5: string='';
  photos6: string='';
  photos7: string='';
  photos8: string='';

  fetchPhotos() {
    let apiUrl = `/api/photos?q=${this.productTitle}`;
    this.http.get(apiUrl).subscribe(
      (data) => {
        this.photosData = data;
        this.photos1=this.photosData.items[0].link;
        this.photos2=this.photosData.items[1].link;
        this.photos3=this.photosData.items[2].link;
        this.photos4=this.photosData.items[3].link;
        this.photos5=this.photosData.items[4].link;
        this.photos6=this.photosData.items[5].link;
        this.photos7=this.photosData.items[6].link;
        this.photos8=this.photosData.items[7].link;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  similarItems: any[] = [];
  maxItemsToShow: number = 5;


  showMoreItems() {
    this.maxItemsToShow = this.similarItems.length;
  }

  showLessItems() {
    this.maxItemsToShow = 5;
  }

  fetchSimilarItems(itemid: number) {
    let apiUrl = `/api/similaritems?itemid=${itemid}`;
    this.http.get(apiUrl).subscribe(
      (data) => {
        this.similaritemsData = data;
        console.log(this.similaritemsData);
      },
      (error) => {
        console.error(error);
      }
    );
  }

  toggleFromText() {
    const fromControl = this.productSearchForm.get('from');
    if (fromControl) {
      if (fromControl.value === 'otherloc') {
        this.productSearchForm.get('fromText')!.enable();
      } else {
        this.productSearchForm.get('fromText')!.disable();
      }
    }
  }  

  isFromTextDisabled() {
    const fromControl = this.productSearchForm.get('from');
    return fromControl ? fromControl.value !== 'otherloc' : true;
  }

  clearForm() {
    this.productSearchForm.reset({ category: 'all', distance: 10, from: 'currloc' });
    // this.ebayData = null;
    this.ebayData.findItemsAdvancedResponse[0].searchResult[0].item = [];
    this.postalcodeData = null;
    this.toggleFromText();
  }

  extractTimeValue(timeLeft: string): string {
    const regex = /P(.*?)D/;
    const match = timeLeft.match(regex);
    return match && match.length > 1 ? match[1] : 'N/A';
  }
}

function keywordValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value;
  const valid = /[^\s]/.test(value);

  if (!valid) {
    return { containsOnlySpaces: true };
  }

  return null;
}

// http://localhost:3000