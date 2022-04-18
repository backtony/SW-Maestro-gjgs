import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import GeneralButton from '../../../../../components/GeneralButton';

interface GroupTimeBlockProps {
  onClickTime: (time: string) => void;
  onClickTimeBig: () => void;
  time: string[];
}

export default class GroupTimeBlock extends React.Component<
  GroupTimeBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.container}>시간</Text>
        <View style={this.styles.innerContainer}>
          <GeneralButton
            title={'오전'}
            onClick={() => {
              this.props.onClickTime('MORNING');
            }}
            clicked={this.props.time.includes('MORNING')}
          />
          <GeneralButton
            title={'오후'}
            onClick={() => {
              this.props.onClickTime('NOON');
            }}
            clicked={this.props.time.includes('NOON')}
          />
          <GeneralButton
            title={'저녁'}
            onClick={() => {
              this.props.onClickTime('AFTERNOON');
            }}
            clicked={this.props.time.includes('AFTERNOON')}
          />
          <GeneralButton
            title={'무관'}
            onClick={() => {
              this.props.onClickTimeBig();
            }}
            clicked={(() => {
              return (
                JSON.stringify(this.props.time.sort()) ===
                JSON.stringify(['AFTERNOON', 'MORNING', 'NOON'])
              );
            })()}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginTop: 20,
      marginBottom: 10,
    },
    innerContainer: {flexDirection: 'row'},
  });
}
