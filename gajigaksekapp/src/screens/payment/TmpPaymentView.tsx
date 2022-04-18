import Certification from '@/services/payment/IamPortCertification';
import Payment from '@/services/payment/IamPortPayment';
import IamPortPayment2 from '@/services/payment/IamPortPayment2';
import React from 'react';
import {View} from 'react-native';
import WebView from 'react-native-webview';

export default class TmpPaymentView extends React.Component {
  render() {
    return (
      <View style={{backgroundColor: 'red', flex: 1}}>
        <IamPortPayment2 />
      </View>
    );
  }
}
