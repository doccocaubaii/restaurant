import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { firstValueFrom, Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { RxStompService } from '../../../../rxStomp/rx-stomp.service';
import { ProductService } from '../../service/product.service';
import { Page } from '../../../const/customer-order.const';

@Component({
  selector: 'jhi-khachqr-sc',
  templateUrl: './khachqr-sc.component.html',
  styleUrls: ['./khachqr-sc.component.scss']
})
export class KhachqrScComponent implements OnInit, OnDestroy {
  isAdmin = false;
  idAmin: any = 0;
  idTable: any = 0;
  orderSelected: any = {
    totalAmount: 0,
    products: [],
    deliveryType: 1
  };
  menuType = 0;
  receivedMessages: any[] = [];
  objectInfo: any = {};
  message: string = '';
  listProduct: any = [];
  loading = true;
  filterProduct: any = { page: Page.PAGE_NUMBER, size: 15, companyId: null };
  private topicSubscription: Subscription | undefined;

  constructor(
    protected productService: ProductService,
    private rxStompService: RxStompService,
    private route: ActivatedRoute,
    private toast: ToastrService) {
  }

  async ngOnInit(): Promise<void> {
    try {
      const params: ParamMap = await firstValueFrom(this.route.paramMap);
      this.idAmin = params.get('idA');
      this.idTable = params.get('idT');
      this.filterProduct.companyId = this.idAmin;
      console.log('idAmin:', this.idAmin);
      console.log('idTable:', this.idTable);
      this.topicSubscription = this.rxStompService.watch(`/topic/messages/${this.idAmin}/${this.idTable}`).subscribe((res: any) => {
        let data = JSON.parse(res.body);
        this.receivedMessages.push(data);
      });
      this.getListProduct();
      // Sử dụng idA và idB ở đây
    } catch (error) {
      console.error('Error fetching route params:', error);
    }
  }

  getListProduct() {
    this.productService.query(this.filterProduct).subscribe(res => {
      this.loading = false;
      const result = res.body;
      result?.forEach(item => {
        this.listProduct?.push(item);
      });
    });
  }

  ngOnDestroy() {
    if (this.topicSubscription) this.topicSubscription.unsubscribe();
  }

  onSendMessage() {
    if (!this.message) return;
    if (!this.idAmin || !this.idTable) {
      this.toast.error('Error url not valid!');
      console.log('adminId = ' + this.idAmin);
      console.log('tableId = ' + this.idTable);
      return;
    }
    let senderObj = {
      type: 1,
      userId: this.idAmin,
      tableId: this.idTable,
      content: this.message
    };
    this.rxStompService.publish({ destination: '/app/sendMessage', body: JSON.stringify(senderObj) });
    this.message = '';
  }


  addProduct() {

  }

  loadMore($event: Event) {

  }
}
