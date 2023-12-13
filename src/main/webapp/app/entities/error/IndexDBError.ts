export class IndexDBError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'IndexDBError';
    Object.setPrototypeOf(this, IndexDBError.prototype); // Đảm bảo rằng IndexDBError là một instance của Error
  }
}
