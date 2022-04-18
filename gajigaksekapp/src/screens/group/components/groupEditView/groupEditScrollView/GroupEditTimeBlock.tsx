import GeneralButton from '@/components/GeneralButton';
import React from 'react';
import {Text, View} from 'react-native';

interface GroupEditTimeBlockProps {
  time: string[];
  onClickTime: (time: string) => void;
  onClickTimeBig: () => void;
}

export default class GroupEditTimeBlock extends React.Component<
  GroupEditTimeBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <Text
          style={{
            fontSize: 14,
            lineHeight: 16,
            fontFamily: 'NotoSansCJKkr-Regular',
            marginTop: 20,
            marginBottom: 10,
          }}>
          시간
        </Text>
        <View style={{flexDirection: 'row'}}>
          <GeneralButton
            title={'오전'}
            onClick={() => {
              this.props.onClickTime('오전');
            }}
            clicked={this.props.time.includes('오전')}
          />
          <GeneralButton
            title={'오후'}
            onClick={() => {
              this.props.onClickTime('오후');
            }}
            clicked={this.props.time.includes('오후')}
          />
          <GeneralButton
            title={'저녁'}
            onClick={() => {
              this.props.onClickTime('저녁');
            }}
            clicked={this.props.time.includes('저녁')}
          />
          <GeneralButton
            title={'무관'}
            onClick={() => {
              this.props.onClickTimeBig();
            }}
            clicked={(() => {
              return (
                JSON.stringify(this.props.time.sort()) ===
                JSON.stringify(['오전', '오후', '저녁'])
              );
            })()}
          />
        </View>
      </View>
    );
  }
}
