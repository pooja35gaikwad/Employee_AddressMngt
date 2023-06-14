export class Employee {
  id: number;
  firstName: string;
  lastName: string;
  emailId: string;
  addresses: Address[];
}

export class Address {
  type: string;
  line1: string;
  line2: string;
  city: string;
  state: string;
  postalCode: string;
}
