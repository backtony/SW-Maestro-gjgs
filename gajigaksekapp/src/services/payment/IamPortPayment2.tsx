import React from 'react';

import IMP from 'iamport-react-native';

import Loading from './Loading';
import User from '../login/User';
import {impUid} from '@/utils/commonParam';

const data = {
  pg: 'html5_inicis',
  pay_method: 'card',
  name: '아임포트 결제데이터 분석',
  merchant_uid: `mid_${new Date().getTime()}`,
  amount: '1',
  buyer_name: '홍길동',
  buyer_tel: '01012345678',
  buyer_email: 'example@naver.com',
  buyer_addr: '서울시 강남구 신사동 661-16',
  buyer_postcode: '06018',
  app_scheme: 'gajigaksek',
  escrow: false,
  // [Deprecated v1.0.3]: m_redirect_url
};

interface IamPortPayment2Props {
  orderId: number;
  data: any;
  callback: (response: any) => void;
}

export default class IamPortPayment2 extends React.Component<
  IamPortPayment2Props,
  {}
> {
  constructor(props: any) {
    super(props);
    console.log('hoho: ', this.props);
  }
  render() {
    return (
      <IMP.Payment
        userCode={impUid} // 가맹점 식별코드
        //tierCode={'AAA'} // 티어 코드: agency 기능 사용자에 한함
        loading={<Loading />} // 로딩 컴포넌트
        data={this.props.data} // 결제 데이터
        callback={this.props.callback} // 결제 종료 후 콜백
      />
    );
  }
}
