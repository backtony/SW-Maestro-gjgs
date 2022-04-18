import React from 'react';
import {TouchableOpacity, View, Text} from 'react-native';

export default class QuestionItem extends React.Component<
  {
    lectureName: string;
    title: string;
    createdAt: string;
    pending: boolean;
    bottomBorder: boolean;
  },
  {}
> {
  render() {
    return (
      <View
        style={[
          {
            flexDirection: 'row',
            justifyContent: 'space-between',
            height: 80,
            alignItems: 'center',
            marginLeft: 20,
          },
          this.props.bottomBorder
            ? {
                borderBottomWidth: 1,
                borderBottomColor: '#f5f5f7',
              }
            : {},
        ]}>
        <View style={{justifyContent: 'center'}}>
          <Text
            style={{
              fontSize: 12,
              lineHeight: 14,
              fontFamily: 'NotoSansCJKkr-Regular',
              color: '#4a4a4c',
            }}>
            {this.props.lectureName}
          </Text>
          <Text
            style={{
              fontSize: 16,
              lineHeight: 18,
              fontFamily: 'NotoSansCJKkr-Bold',
              color: '#1d1d1f',
            }}>
            {this.props.title}
          </Text>
          <Text
            style={{
              fontSize: 12,
              lineHeight: 14,
              fontFamily: 'NotoSansCJKkr-Regular',
              color: '#8e8e8f',
            }}>
            {this.props.createdAt}
          </Text>
        </View>
        <View
          style={[
            {
              height: 28,
              backgroundColor: '#4f6cff',
              borderRadius: 16,
              justifyContent: 'center',
              paddingHorizontal: 11,
              paddingTop: 4,
              marginRight: 20,
            },
            this.props.pending ? {} : {backgroundColor: '#d2d2d2'},
          ]}>
          <Text
            style={{
              fontSize: 14,
              lineHeight: 16,
              fontFamily: 'NotoSansCJKkr-Bold',
              color: '#ffffff',
            }}>
            {this.props.pending ? '대기' : '완료'}
          </Text>
        </View>
      </View>
    );
  }
}
