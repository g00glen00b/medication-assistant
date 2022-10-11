import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {ValueProvider} from "@angular/core";

export const spyOnClass = <T>(
  spiedClass: NewableFunction,
  properties: Record<string, any> = {}
): jasmine.SpyObj<T> => {
  const prototype = spiedClass.prototype;

  const methods = Object.getOwnPropertyNames(prototype)
    .map((name) => [name, Object.getOwnPropertyDescriptor(prototype, name)])
    .filter(([, descriptor]) => {
      return (descriptor as PropertyDescriptor).value instanceof Function;
    })
    .map(([name]) => name);

  return { ...properties, ...jasmine.createSpyObj('spy', [...methods]) };
};

export const provideMock = <T>(
  spiedClass: NewableFunction,
  properties: Record<string, any> = {}
): ValueProvider => {
  return {
    provide: spiedClass,
    useValue: spyOnClass(spiedClass, properties),
  };
};

export const provideMatDialogData = <T>(
  properties: Record<string, any> = {}
): ValueProvider => {
  return {
    provide: MAT_DIALOG_DATA,
    useValue: properties,
  };
};
