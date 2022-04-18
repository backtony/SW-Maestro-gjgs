// RootNavigation.js
import * as React from 'react';

import {NavigationContainerRef} from '@react-navigation/native';

export const navigationRef: React.RefObject<NavigationContainerRef> =
  React.createRef();

export function navigate(name: any, params: any) {
  navigationRef.current?.navigate(name, params);
}

// add other navigation functions that you need and export them
