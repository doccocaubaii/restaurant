export class ConvertResponse {
  static getDataFromServer(response: any, getMessage: boolean): any {
    let bodyResult;
    if (response.status || response.ok) {
      bodyResult = response.body;
    }
    if (getMessage) {
      return response.body.message[0].message;
    }
    return bodyResult;
  }
}
