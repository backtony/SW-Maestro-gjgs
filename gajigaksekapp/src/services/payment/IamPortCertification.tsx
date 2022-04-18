import React from 'react';
/* 아임포트 본인인증 모듈을 불러옵니다. */
import IMP from 'iamport-react-native';

/* 로딩 컴포넌트를 불러옵니다. */
import Loading from './Loading';

export function Certification({navigation}) {
  /* [필수입력] 본인인증 종료 후, 라우터를 변경하고 결과를 전달합니다. */
  function callback(response: any) {
    console.log('iam portt certification res: ', response);
    // navigation.replace('CertificationResult', response);
  }

  /* [필수입력] 본인인증에 필요한 데이터를 입력합니다. */
  const data = {
    merchant_uid: `mid_${new Date().getTime()}`,
    company: '아임포트',
    carrier: 'SKT',
    name: '홍길동',
    phone: '01012341234',
    min_age: '',
  };

  return (
    <IMP.Certification
      userCode={'imp52333583'} // 가맹점 식별코드
      //tierCode={'AAA'} // 티어 코드: agency 기능 사용자에 한함
      loading={<Loading />} // 로딩 컴포넌트
      data={data} // 본인인증 데이터
      callback={callback} // 본인인증 종료 후 콜백
    />
  );
}

export default Certification;
