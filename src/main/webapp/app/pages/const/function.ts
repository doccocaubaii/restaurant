export function validVariable(value: any) {
  if (value !== null && value !== undefined && value.toString().trim() !== '') {
    return true;
  }
  return false;
}

export function take_decimal_number(num, n) {
  const base = 10 ** n;
  const result = Math.round(num * base) / base;
  return result;
}
