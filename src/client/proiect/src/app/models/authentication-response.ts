import {HttpResponse} from "@angular/common/http";
import {AuthorityModel} from "./authority-model";

export interface AuthenticationResponse extends HttpResponse<AuthenticationResponse>{
   userId: number;
   email: string;
   firstName: string;
   token: string;
   authorities: Array<AuthorityModel>;
}
