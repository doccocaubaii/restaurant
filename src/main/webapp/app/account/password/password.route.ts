import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PasswordComponent } from './password.component';
import { CHANGE_PASSWORD } from '../../constants/app.routing.constants';

export const passwordRoute: Route = {
  path: CHANGE_PASSWORD,
  component: PasswordComponent,
  data: {
    pageTitle: 'Đổi mật khẩu',
  },
  canActivate: [UserRouteAccessService],
};
