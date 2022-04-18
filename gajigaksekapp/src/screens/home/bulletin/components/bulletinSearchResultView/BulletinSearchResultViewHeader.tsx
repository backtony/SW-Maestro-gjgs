import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, TextInput, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface BulletinSearchResultViewHeaderProps {
  navigation: any;
  keyword: string;
  setKeyword: (text: string) => void;
  onSubmit: () => void;
}

export default class BulletinSearchResultViewHeader extends React.Component<
  BulletinSearchResultViewHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <Button
          icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
          buttonStyle={{backgroundColor: 'white'}}
          onPress={() => this.props.navigation.goBack()}
        />
        <View style={this.styles.inputWrapper}>
          <Icon name="search" size={24} color="rgba(0, 0, 0, 0.3)" />
          <TextInput
            style={this.styles.input}
            onChangeText={text => this.props.setKeyword(text)}
            onSubmitEditing={() => this.props.onSubmit()}
            placeholder={'게시글 검색'}
            value={this.props.keyword}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      backgroundColor: '#FFF',
    },
    inputWrapper: {
      flex: 1,
      backgroundColor: '#f5f5f7',
      flexDirection: 'row',
      padding: 10,
      borderRadius: 6,
      marginRight: 20,
      alignItems: 'center',
    },
    input: {
      backgroundColor: '#f5f5f7',
      marginLeft: 8,
    },
  });
}
