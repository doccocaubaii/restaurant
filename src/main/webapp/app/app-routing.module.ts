import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { PosCustomerOrderPage } from './pages/pos/customer-order/customer-order.component';
import { PosCounterCheckoutPage } from './pages/pos/counter-checkout/counter-checkout.component';
import { LoginPage } from './pages/login/login.component';
import { DashboardPage } from './pages/dashboard/dashboard.component';
import { OrderPage } from './pages/don-hang/order.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { KhachqrScComponent } from './pages/pos/customer-order/khachqr-sc/khachqr-sc.component';
import { QrCodeComponent } from './pages/qr-code/qr-code.component';
import { StaffComponent } from './pages/staff/staff.component';
import { KitchenComponent } from './pages/kitchen/kitchen.component';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'tong-quan', component: DashboardPage, data: { title: 'Dashboard V3' } },
  { path: 'don-hang', component: OrderPage, data: { title: 'Order' } },
  { path: 'bep', component: KitchenComponent, data: { title: 'Kitchen' } },
  { path: 'login', component: LoginPage, data: { title: 'Login' } },
  { path: 'pos/ban-hang/:idA/:idT', component: KhachqrScComponent, data: { title: 'Order For Customer' } },
  { path: 'pos/ban-hang', component: PosCustomerOrderPage, data: { title: 'Pos customer order page' } },
  { path: 'pos/ban-hang/:id', component: PosCustomerOrderPage, data: { title: 'Pos customer order page' } },
  { path: 'pos/qr-code', component: QrCodeComponent, data: { title: 'QR Code' } },
  { path: 'pos/nhan-vien', component: StaffComponent, data: { title: 'Staff' } },
  { path: 'pos/counter-checkout', component: PosCounterCheckoutPage, data: { title: 'Pos counter checkout' } },
  {
    path: 'pos/san-pham',
    loadChildren: () => import('./pages/product/product.module').then(m => m.ProductModule)
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      scrollPositionRestoration: 'enabled',
      anchorScrolling: 'enabled',
      enableTracing: DEBUG_INFO_ENABLED
    }),
    BrowserModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
