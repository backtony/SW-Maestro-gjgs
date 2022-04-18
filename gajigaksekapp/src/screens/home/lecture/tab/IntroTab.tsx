import React from 'react';
import {View, Text, Image, ScrollView, StyleSheet} from 'react-native';

export class FinishedProductInfo {
  finishedProductId: number | undefined;
  order: number | undefined;
  finishedProductImageUrl: string | undefined;
  text: string | undefined;
}

interface IntroTabProps {
  finishedProductList: FinishedProductInfo[];
}

export default class IntroTab extends React.Component<IntroTabProps, {}> {
  render() {
    return (
      <ScrollView style={{flex: 1, backgroundColor: 'white'}}>
        <View style={this.styles.wrapper1}>
          <Text style={this.styles.text1}>클래스 소개</Text>
        </View>
        <Text>부드럽고 촉촉한 스모어쿠키 3종</Text>
        <Text>오레오초코,루토스시나몬/녹차초코칩</Text>
        <Text>3가지 맛 모두 만들어 보실 수 있어요~</Text>
        <Text>(취향에 따라 변경 가능합니다)</Text>
        <Text>수량 1인당 9~10개</Text>
        <Image
          style={{height: 200, borderRadius: 8, marginVertical: 10}}
          source={{
            uri: 'https://images.unsplash.com/photo-1518047601542-79f18c655718?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80',
          }}
        />
        <Text>레시피는 제공되지 않지만 최대한 직접</Text>
        <Text>실습할 수 있도록 준비했어요~</Text>
        <Text>똥손도 금손되어 가는 재미있는</Text>
        <Text>수업입니다~(완전 초급)</Text>
        <Text>(베이킹 많이 해보신 분 들은 너무 쉬울 수 있습니다.)1</Text>
        <View style={this.styles.wrapper2}>
          <Text style={this.styles.text2}>완성작 소개</Text>
        </View>

        {this.props.finishedProductList &&
          this.props.finishedProductList.map((product: FinishedProductInfo) => (
            <View style={this.styles.container}>
              <Image
                style={this.styles.image}
                source={{
                  uri: product.finishedProductImageUrl,
                }}
              />
              <Text style={this.styles.title}>{product.text}</Text>
            </View>
          ))}
      </ScrollView>
    );
  }
  private styles = StyleSheet.create({
    container: {marginBottom: 40},
    title: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginTop: 7,
      marginLeft: 6,
    },
    image: {height: 200, borderRadius: 8, marginVertical: 10},
    wrapper1: {
      flexDirection: 'row',
      marginTop: 20,
      alignItems: 'center',
      marginBottom: 20,
    },
    text1: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 7,
      marginLeft: 6,
    },
    wrapper2: {
      flexDirection: 'row',
      marginTop: 20,
      alignItems: 'center',
      marginBottom: 20,
    },
    text2: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 7,
      marginLeft: 6,
    },
  });
}
