import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'report',
        data: { pageTitle: 'easyPosFrontEndApp.report.home.title' },
        loadChildren: () => import('../report/report.module').then(m => m.ReportModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
