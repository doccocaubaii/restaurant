import { Injectable } from '@angular/core';
import { Observable, Observer, Subject, Subscription } from 'rxjs';
import { filter, share } from 'rxjs/operators';

export class EventWithContent<T> {
  constructor(public name: string, public content: T) {}
}

export interface Message {
  type: string;
  payload: any;
}

/**
 * An utility class to manage RX events
 */
@Injectable({
  providedIn: 'root',
})
export class EventManager {
  observable: Observable<EventWithContent<unknown> | string>;
  observer?: Observer<EventWithContent<unknown> | string>;

  constructor() {
    this.observable = new Observable((observer: Observer<EventWithContent<unknown> | string>) => {
      this.observer = observer;
    }).pipe(share());
  }

  private eventSubject = new Subject<EventWithContent<unknown> | string>();

  broadcast2(event: EventWithContent<unknown> | string): void {
    this.eventSubject.next(event);
  }

  observeEvents(): Observable<EventWithContent<unknown> | string> {
    return this.eventSubject.asObservable();
  }

  /**
   * Method to broadcast the event to observer
   */
  broadcast(event: EventWithContent<unknown> | string): void {
    if (this.observer) {
      this.observer.next(event);
    }
    this.eventSubject.next(event);
  }

  /**
   * Method to subscribe to an event with callback
   * @param eventNames  Single event name or array of event names to what subscribe
   * @param callback    Callback to run when the event occurs
   */
  subscribe(eventNames: string | string[], callback: (event: EventWithContent<unknown> | string) => void): Subscription {
    if (typeof eventNames === 'string') {
      eventNames = [eventNames];
    }
    return this.observable
      .pipe(
        filter((event: EventWithContent<unknown> | string) => {
          for (const eventName of eventNames) {
            if ((typeof event === 'string' && event === eventName) || (typeof event !== 'string' && event.name === eventName)) {
              return true;
            }
          }
          return false;
        })
      )
      .subscribe(callback);
  }

  /**
   * Method to unsubscribe the subscription
   */
  destroy(subscriber: Subscription): void {
    subscriber?.unsubscribe();
  }
}
