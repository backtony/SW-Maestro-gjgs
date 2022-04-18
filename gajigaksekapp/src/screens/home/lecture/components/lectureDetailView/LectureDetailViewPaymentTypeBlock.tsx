import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';

interface LectureDetailViewPaymentTypeBlockProps {
  applyType: string;
  setApplyType: (applyType: string) => void;
}

export default class LectureDetailViewPaymentTypeBlock extends React.Component<
  LectureDetailViewPaymentTypeBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <View style={this.styles.titleWrapper}>
          <Text style={this.styles.title}>신청 단위 선택</Text>
        </View>
        <View
          style={{
            flexDirection: 'row',
          }}>
          <TouchableOpacity
            style={[
              this.styles.button,
              this.props.applyType === 'PERSONAL'
                ? {backgroundColor: '#4f6cff'}
                : {},
            ]}
            onPress={() => this.props.setApplyType('PERSONAL')}>
            <Text
              style={[
                this.styles.text,
                this.props.applyType === 'PERSONAL' ? {color: '#FFF'} : {},
              ]}>
              개인
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[
              this.styles.button,
              this.props.applyType === 'TEAM'
                ? {backgroundColor: '#4f6cff'}
                : {},
            ]}
            onPress={() => this.props.setApplyType('TEAM')}>
            <Text
              style={[
                this.styles.text,
                this.props.applyType === 'TEAM' ? {color: '#FFF'} : {},
              ]}>
              그룹
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    titleWrapper: {
      flexDirection: 'row',
      marginTop: 40,
      alignItems: 'center',
      marginBottom: 20,
      marginHorizontal: 20,
    },
    title: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 7,
      marginLeft: 6,
    },
    button: {
      backgroundColor: '#f5f5f7',
      height: 44,
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: 6,
      flex: 1,
      marginRight: 20,
      marginLeft: 20,
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      color: '#4a4a4c',
    },
  });
}
