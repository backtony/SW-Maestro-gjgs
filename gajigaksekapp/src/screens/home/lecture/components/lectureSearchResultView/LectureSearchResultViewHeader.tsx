import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, TextInput, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface LectureSearchResultViewHeaderProps {
  keyword: string;
  setKeyword: (keyword: string) => void;
  onSubmit: () => void;
  navigation: any;
}

export default class LectureSearchResultViewHeader extends React.Component<
  LectureSearchResultViewHeaderProps,
  {}
> {
  render() {
    return (
      <View
        style={[
          {
            marginTop: STATUSBAR_HEIGHT,
          },
          this.styles.container,
        ]}>
        <Button
          icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
          buttonStyle={{backgroundColor: 'white'}}
          onPress={() => this.props.navigation.goBack()}
        />
        <View style={this.styles.textInputWrapper}>
          <Icon name="search" size={24} color="rgba(0, 0, 0, 0.3)" />
          <TextInput
            style={this.styles.textInput}
            onChangeText={text => this.props.setKeyword(text)}
            onSubmitEditing={() => {
              this.props.onSubmit();
            }}
            placeholder={'클래스 검색'}
            value={this.props.keyword}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      backgroundColor: '#FFF',
    },
    textInputWrapper: {
      flex: 1,
      backgroundColor: '#f5f5f7',
      flexDirection: 'row',
      padding: 10,
      borderRadius: 6,
      marginRight: 20,
      alignItems: 'center',
    },
    textInput: {
      backgroundColor: '#f5f5f7',
      marginLeft: 8,
    },
  });
}
